package com.morpho.android.ws.impl.sqlite;

import android.content.Context;
import android.os.AsyncTask;

import com.morpho.android.R;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Buses;

import java.util.List;

public class BusesSQLite implements Buses {

    private final Context context;

    public BusesSQLite(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public FetchSQLiteRequest fetch() {
        return new FetchSQLiteRequest(context);
    }

    private static class FetchSQLiteRequest implements Buses.FetchRequest {

        private final Context context;
        
        private String query;
        private int limit;

        private FetchSQLiteRequest(Context context) {
            this.context = context;
        }

        @Override
        public FetchRequest allBusNames() {
            query = context.getString(R.string.all_bus_names_query);
            return this;
        }

        @Override
        public FetchRequest limitTo(int limit) {
            if (limit > 0) {
                this.limit = limit;
                query += context.getString(R.string.limit_query_fragment);
            }
            return this;
        }

        @Override
        public void loadBusNames(final AsyncTaskAdapter<List<String>> adapter) {
            new BusNamesLoader(context) {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (adapter != null) adapter.onPreExecute();
                }
                
                @Override
                protected void onPostExecute(List<String> result) {
                    super.onPostExecute(result);
                    if (adapter != null) adapter.onPostExecute(result);
                }
            }.execute(query, limit);
        }
        
        private static class BusNamesLoader  extends AsyncTask<Object, Void, List<String>> {

            private final Context context;

            private SQLiteTemplate sqliteTemplate;
            
            private BusNamesLoader(Context context) {
                this.context = context.getApplicationContext();
            }

            @Override
            protected void onPreExecute() {
                sqliteTemplate = new SQLiteTemplate(new MorphoSQLiteOpenHelper(context));
            }

            @Override
            protected List<String> doInBackground(Object... params) {
                String query = (String) params[0];
                String[] queryParams = new String[params.length-1];
                for (int i = 1; i < params.length; i++) queryParams[i-1] = params[i].toString();
                return sqliteTemplate.queryForList(
                        query, 
                        queryParams, 
                        new SQLiteTemplate.SingleColumnRowMapper());
            }
            
        }
    }
}