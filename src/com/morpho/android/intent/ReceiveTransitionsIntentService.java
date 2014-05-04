package com.morpho.android.intent;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.morpho.android.R;
import com.morpho.android.data.Schedule;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Schedules;
import com.morpho.android.ws.impl.MorphoClientFactory;

import java.util.List;

public class ReceiveTransitionsIntentService extends IntentService {

    private static final String TAG = ReceiveTransitionsIntentService.class.getSimpleName();

    private static int notificationId;
    
    private NotificationManagerCompat notificationManager;
    
    public ReceiveTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent");
        if (LocationClient.hasError(intent)) {
            int errorCode = LocationClient.getErrorCode(intent);
            Log.e(TAG, "Location Services error: " + Integer.toString(errorCode));
        } else {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                long stationId = Long.parseLong(LocationClient.getTriggeringGeofences(intent).get(0).getRequestId());
                Log.d(TAG, "Entered in:" + LocationClient.getTriggeringGeofences(intent));
                MorphoClientFactory morphoClientFactory = new MorphoClientFactory(getApplicationContext());
                Schedules schedules = morphoClientFactory.get(Schedules.class);
                schedules.fetch()
                    .comingSchedules(stationId)
                    .limitTo(3)
                    .loadSchedules(new AsyncTaskAdapter<List<Schedule>>() {

                        @Override
                        public void onPostExecute(List<Schedule> result) {
                            Log.d(TAG, "Result: " + result);
                            if (!result.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                for (Schedule s : result) {
                                    sb.append("Ruta ")
                                        .append(s.getRoute().getName())
                                        .append(" Horario: ")
                                        .append(DateFormat.getTimeFormat(getApplicationContext()).format(s.getDepartureAt()))
                                        .append('\n');
                                }
                                
                                Log.d(TAG, sb.toString());
                                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                    .setContentTitle("Horarios de autobuses próximos")
                                    .setContentText(sb.toString())
                                    .setTicker("Terminal X de autobuses")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .build();
                                notificationManager.notify(++notificationId, notification);
                            }
                        }
                    });
            } else Log.e(TAG, "Geofence transition error: " + Integer.toString(transitionType));
        }
    }
}