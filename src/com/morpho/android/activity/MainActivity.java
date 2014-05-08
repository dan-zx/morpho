package com.morpho.android.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.morpho.android.R;
import com.morpho.android.data.GeoPoint;
import com.morpho.android.data.Station;
import com.morpho.android.intent.ReceiveTransitionsIntentService;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Stations;
import com.morpho.android.ws.impl.MorphoClientFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity 
    extends FragmentActivity 
    implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener, LocationClient.OnRemoveGeofencesResultListener, LocationListener {

    private static final long FIVE_MINS = 1000L * 60L * 5L;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOCK_LOCATION_PROVIDER = "MOCK";

    private boolean isUsingMockLocations;
    private GoogleMap map;
    private Marker userCurrentLocationMarker;
    private LocationClient locationClient;
    private MorphoClientFactory morphoClientFactory;
    private List<Geofence> geofences;
    private PendingIntent transitionsIntentService;
    private LocationRequest locationRequest;
    private Switch mockLocationsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        morphoClientFactory = new MorphoClientFactory(this);
        setUpBusChoices();
        setUpMockLocationsSwitch();
        setUpMapIfNeeded();
        setUpLocationClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }
    
    @Override
    protected void onStop() {
        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
            if (transitionsIntentService != null) locationClient.removeGeofences(transitionsIntentService, this);
            locationClient.disconnect();
        }
        super.onStop();
    }

    private void setUpLocationClient() {
        locationClient = new LocationClient(this, this, this);
    }

    private long ageOf(Location location) {
        return (SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos()) / 1000000L;
    }
    
    private PendingIntent getTransitionPendingIntent() {
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setUpGeofences(Location currentLocation) {
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(currentLocation.getLatitude());
        geoPoint.setLongitude(currentLocation.getLongitude());
        Stations stations = morphoClientFactory.get(Stations.class);
        stations.fetch()
            .nearestStations(geoPoint)
            .limitTo(5)
            .loadNearestStations(new AsyncTaskAdapter<List<Station>>() {
                    public void onPostExecute(List<Station> result) {
                        geofences = new ArrayList<>(result.size());
                        for (Station s : result) {
                            Geofence g = new Geofence.Builder()
                                .setRequestId(s.getId().toString())
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                                .setLoiteringDelay(1000*60*1)
                                .setCircularRegion(
                                        s.getArea().getLocation().getLatitude(), 
                                        s.getArea().getLocation().getLongitude(),
                                        s.getArea().getRadius())
                                .build();
                            map.addCircle(
                                    new CircleOptions()
                                        .center(new LatLng(s.getArea().getLocation().getLatitude(), s.getArea().getLocation().getLongitude()))
                                        .radius(s.getArea().getRadius())
                                        .fillColor(Color.parseColor("#aa800080"))
                                        .strokeColor(Color.parseColor("#ff800080"))
                                        .strokeWidth(1f));
                            geofences.add(g);
                        }
                        transitionsIntentService = getTransitionPendingIntent();
                        locationClient.addGeofences(geofences, transitionsIntentService, MainActivity.this);
                    }
                });
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.054359, -98.283107), 18));
                map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    
                    @Override
                    public void onMapLongClick(LatLng point) {
                        if (locationClient.isConnected() && isUsingMockLocations) {
                            Location newLocation = new Location(MOCK_LOCATION_PROVIDER);
                            newLocation.setLatitude(point.latitude);
                            newLocation.setLongitude(point.longitude);
                            newLocation.setTime(System.nanoTime());
                            newLocation.setAccuracy(0f);
                            newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                            locationClient.setMockLocation(newLocation);
                        }
                    }
                });
            }
        }
    }
    
    private void setUpMockLocationsSwitch() {
        mockLocationsSwitch = (Switch) findViewById(R.id.mock_locations_switch);
        mockLocationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (locationClient.isConnected()) {
                        locationClient.setMockMode(true);
                        isUsingMockLocations = true;
                    } else {
                        isUsingMockLocations = false;
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(), 
                                "GPS no esta listo aún", 
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    if (locationClient.isConnected()) {
                        locationClient.setMockMode(false);
                        isUsingMockLocations = false;
                    }
                }
            }
        });
    }

    private void setUpBusChoices() {
        // TODO: setup all possible choices
    }

    private void updateUserCurrentLocationMarker(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        if (userCurrentLocationMarker == null) {
            userCurrentLocationMarker = map.addMarker(
                    new MarkerOptions()
                        .draggable(false)
                        .position(latlng)
                        .title("Aquí estas"));
        } else userCurrentLocationMarker.setPosition(latlng);
        userCurrentLocationMarker.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    @Override
    public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
        if (LocationStatusCodes.SUCCESS == statusCode) Log.i(TAG, "Geofences set");
        else Log.e(TAG, "Geofences where not set correctly, errorCode" + statusCode);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) { 
        Log.e(TAG, "onConnectionFailed: " + result);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected");
        Location lastLocation = locationClient.getLastLocation();
        if (lastLocation != null && ageOf(lastLocation) <= FIVE_MINS) {
            updateUserCurrentLocationMarker(lastLocation);
            if (geofences == null) setUpGeofences(lastLocation);
        }
        locationRequest = LocationRequest.create()
                .setFastestInterval(0)
                .setInterval(0)
                .setSmallestDisplacement(5f)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    public void onDisconnected() { 
        Log.v(TAG, "onDisconnected");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location);
        if (geofences == null) setUpGeofences(location);
        updateUserCurrentLocationMarker(location);
    }

    @Override
    public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent pendingIntent) {
        Log.v(TAG, "onRemoveGeofencesByPendingIntentResult");
    }

    @Override
    public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
        Log.v(TAG, "onRemoveGeofencesByRequestIdsResult");
    }
}