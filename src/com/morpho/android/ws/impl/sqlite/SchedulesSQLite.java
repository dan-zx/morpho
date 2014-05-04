package com.morpho.android.ws.impl.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.morpho.android.R;
import com.morpho.android.data.GeoArea;
import com.morpho.android.data.GeoPoint;
import com.morpho.android.data.Route;
import com.morpho.android.data.Schedule;
import com.morpho.android.data.Station;
import com.morpho.android.data.Schedule.ServiceDay;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Schedules;
import com.morpho.android.ws.impl.sqlite.SQLiteTemplate.RowMapper;
import com.morpho.android.ws.impl.sqlite.SQLiteUtils.TimeString;

import java.util.Calendar;
import java.util.List;

public class SchedulesSQLite implements Schedules {

    private final Context context;

    public SchedulesSQLite(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public FetchSQLiteRequest fetch() {
        return new FetchSQLiteRequest(context);
    }
    
    private static class FetchSQLiteRequest implements Schedules.FetchRequest {

        private final Context context;
        
        private String query;
        private ServiceDay serviceDay;
        private long stationId;
        private int limit;

        private FetchSQLiteRequest(Context context) {
            this.context = context;
        }
        
        @Override
        public FetchRequest comingSchedules(long stationId) {
            this.stationId = stationId;
            limit = DEFAULT_RESULT_LIMIT;
            query = context.getString(R.string.coming_schedules_query).replaceAll("\\\\'", "'");
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_WEEK); 
            switch (day) {
                case Calendar.SATURDAY: serviceDay = ServiceDay.SATURDAY; break;
                case Calendar.SUNDAY: serviceDay = ServiceDay.SUNDAY; break;
                default: serviceDay = ServiceDay.WEEKDAY; break;
            }
            return this;
        }

        @Override
        public FetchRequest on(ServiceDay serviceDay) {
            this.serviceDay = serviceDay;
            return this;
        }

        @Override
        public FetchRequest limitTo(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public void loadSchedules(final AsyncTaskAdapter<List<Schedule>> adapter) {
            new SchedulesLoader(context) {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (adapter != null) adapter.onPreExecute();
                }
                
                @Override
                protected void onPostExecute(List<Schedule> result) {
                    super.onPostExecute(result);
                    if (adapter != null) adapter.onPostExecute(result);
                }
            }.execute(query, stationId, limit);
        }
        
        private static class SchedulesLoader extends AsyncTask<Object, Void, List<Schedule>> {

            private final Context context;

            private SQLiteTemplate sqliteTemplate;
            
            private SchedulesLoader(Context context) {
                this.context = context.getApplicationContext();
            }

            @Override
            protected void onPreExecute() {
                sqliteTemplate = new SQLiteTemplate(new MorphoSQLiteOpenHelper(context));
            }

            @Override
            protected List<Schedule> doInBackground(Object... params) {
                String query = (String) params[0];
                String[] queryParams = new String[params.length-1];
                for (int i = 1; i < params.length; i++) queryParams[i-1] = params[i].toString();
                return sqliteTemplate.queryForList(query, queryParams, 
                        new RowMapper<Schedule>() {
                            @Override
                            public Schedule mapRow(Cursor cursor, int rowNum) {
                                GeoPoint geoPoint = new GeoPoint();
                                geoPoint.setLatitude(SQLiteUtils.getDouble(cursor, "latitude"));
                                geoPoint.setLongitude(SQLiteUtils.getDouble(cursor, "longitude"));
                                GeoArea geoAra = new GeoArea();
                                geoAra.setLocation(geoPoint);
                                geoAra.setRadius(SQLiteUtils.getFloat(cursor, "radius"));
                                Station station = new Station();
                                station.setId(SQLiteUtils.getLong(cursor, "station_id"));
                                station.setArea(geoAra);
                                station.setName(SQLiteUtils.getString(cursor, "station_name"));
                                Route route = new Route();
                                route.setId(SQLiteUtils.getLong(cursor, "route_id"));
                                route.setStation(station);
                                route.setName(SQLiteUtils.getString(cursor, "route_name"));
                                Schedule schedule = new Schedule();
                                schedule.setId(SQLiteUtils.getLong(cursor, "id"));
                                schedule.setRoute(route);
                                schedule.setDepartureAt(SQLiteUtils.getDateFromString(cursor, "departure_at", TimeString.TIME));
                                schedule.setServiceDay(SQLiteUtils.getEnum(cursor, "service_day", ServiceDay.class));
                                return schedule;
                            }
                        }
                );
            }
            
        }
    }
}
