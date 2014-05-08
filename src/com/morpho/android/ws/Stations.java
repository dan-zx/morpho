package com.morpho.android.ws;

import com.morpho.android.data.GeoPoint;
import com.morpho.android.data.Station;

import java.util.List;

public interface Stations {

    FetchRequest fetch();

    interface FetchRequest extends MorphoRequest {

        FetchRequest nearestStations(GeoPoint location);
        FetchRequest limitTo(int limit);
        void loadNearestStations(AsyncTaskAdapter<List<Station>> adapter);
    }
}