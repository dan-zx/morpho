package com.morpho.android.ws;

import java.util.List;

public interface Buses {

    FetchRequest fetch();

    interface FetchRequest extends MorphoRequest {

        FetchRequest allBusNames();
        FetchRequest limitTo(int limit);
        void loadBusNames(AsyncTaskAdapter<List<String>> adapter);
    }
}