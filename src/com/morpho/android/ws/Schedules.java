package com.morpho.android.ws;

import com.morpho.android.data.Schedule;
import com.morpho.android.data.Schedule.ServiceDay;

import java.util.List;

public interface Schedules {
    
    FetchRequest fetch();
    
    interface FetchRequest extends MorphoRequest {
        
        FetchRequest comingSchedules(long stationId);
        FetchRequest on(ServiceDay serviceDay);
        FetchRequest only(String... busNames);
        FetchRequest limitTo(int limit);
        void loadSchedules(AsyncTaskAdapter<List<Schedule>> adapter);
    }
}