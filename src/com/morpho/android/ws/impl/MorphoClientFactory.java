package com.morpho.android.ws.impl;

import android.content.Context;

import com.morpho.android.ws.Buses;
import com.morpho.android.ws.Schedules;
import com.morpho.android.ws.Stations;
import com.morpho.android.ws.impl.sqlite.BusesSQLite;
import com.morpho.android.ws.impl.sqlite.SchedulesSQLite;
import com.morpho.android.ws.impl.sqlite.StationsSQLite;

public class MorphoClientFactory {

    private final Context context;
    
    public MorphoClientFactory(Context context) {
        this.context = context;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> which) {
        if (which == Schedules.class) return (T) new SchedulesSQLite(context);
        if (which == Stations.class) return (T) new StationsSQLite(context);
        if (which == Buses.class) return (T) new BusesSQLite(context);
        return null;
    }
}