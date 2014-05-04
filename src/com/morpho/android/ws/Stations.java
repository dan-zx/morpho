package com.morpho.android.ws;

import com.morpho.android.data.GeoPoint;
import com.morpho.android.data.Station;

import java.util.List;

public interface Stations {

    FetchRequest fetch();

    static interface FetchRequest {

        int DEFAULT_RESULT_LIMIT = 3;

        FetchRequest nearestStations(GeoPoint location);
        FetchRequest limitTo(int limit);
        void loadNearestStations(AsyncTaskAdapter<List<Station>> adapter);
    }
}