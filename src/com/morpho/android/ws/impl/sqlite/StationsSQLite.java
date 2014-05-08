package com.morpho.android.ws.impl.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.morpho.android.R;
import com.morpho.android.data.GeoArea;
import com.morpho.android.data.GeoPoint;
import com.morpho.android.data.Station;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Stations;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StationsSQLite implements Stations {

    private final Context context;
    
    public StationsSQLite(Context context) {
        this.context = context.getApplicationContext();
    }
    
    @Override
    public FetchSQLiteRequest fetch() {
        return new FetchSQLiteRequest(context);
    }
    
    private static class FetchSQLiteRequest implements Stations.FetchRequest {

        private final Context context;
        
        private String query;
        private GeoPoint location;
        private int limit;        
        
        private FetchSQLiteRequest(Context context) {
            this.context = context;
        }
        
        @Override
        public FetchRequest nearestStations(GeoPoint location) {
            query = context.getString(R.string.all_stations_query).replaceAll("\\\\'", "'");
            this.location = location;
            return this;
        }

        @Override
        public FetchRequest limitTo(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public void loadNearestStations(final AsyncTaskAdapter<List<Station>> adapter) {
            new StationsLoader(context) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (adapter != null) adapter.onPreExecute();
                    }
                    
                    @Override
                    protected void onPostExecute(List<Station> result) {
                        super.onPostExecute(result);
                        Collections.sort(result, new Comparator<Station>() {

                            @Override
                            public int compare(Station lhs, Station rhs) { 
                                double distance1 = distanceBetween(location, lhs.getArea().getLocation());
                                double distance2 = distanceBetween(location, rhs.getArea().getLocation());
                                return distance1 > distance2 ? 1 : distance1 < distance2 ? -1 : 0;
                            }
                            
                            private double distanceBetween(GeoPoint g1, GeoPoint g2) {
                                int earthRadius = 6371;
                                double lat1 = g1.getLatitude();
                                double lat2 = g2.getLatitude();
                                double lon1 = g1.getLongitude();
                                double lon2 = g2.getLongitude();
                                return Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*earthRadius;
                            }
                        });
                        if (limit > 0) result = result.subList(0, limit);
                        if (adapter != null) adapter.onPostExecute(result);
                    }
                }
            .execute(query);
        }

        private static class StationsLoader extends AsyncTask<Object, Void, List<Station>> {

            private final Context context;

            private SQLiteTemplate sqliteTemplate;
            
            private StationsLoader(Context context) {
                this.context = context.getApplicationContext();
            }
            
            @Override
            protected void onPreExecute() {
                sqliteTemplate = new SQLiteTemplate(new MorphoSQLiteOpenHelper(context));
            }
            
            @Override
            protected List<Station> doInBackground(Object... params) {
                String query = (String) params[0];
                String[] queryParams = new String[params.length-1];
                for (int i = 1; i < params.length; i++) queryParams[i-1] = params[i].toString();
                return sqliteTemplate.queryForList(query, queryParams,
                        new SQLiteTemplate.RowMapper<Station>(){ 
                    
                            @Override
                            public Station mapRow(Cursor cursor, int rowNum) {
                                Station s = new Station();
                                GeoArea ga = new GeoArea();
                                ga.setRadius(SQLiteUtils.getFloat(cursor, "radius"));
                                GeoPoint gp = new GeoPoint();
                                ga.setLocation(gp);
                                gp.setLatitude(SQLiteUtils.getDouble(cursor, "latitude"));
                                gp.setLongitude(SQLiteUtils.getDouble(cursor, "longitude"));
                                s.setId(SQLiteUtils.getLong(cursor, "id"));
                                s.setArea(ga);
                                s.setName(SQLiteUtils.getString(cursor, "name"));
                                return s;
                            }
                    });
            }
        }
    }
}