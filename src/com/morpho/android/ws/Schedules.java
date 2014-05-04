package com.morpho.android.ws;

import com.morpho.android.data.Schedule;
import com.morpho.android.data.Schedule.ServiceDay;

import java.util.List;

public interface Schedules {
    
    FetchRequest fetch();
    
    interface FetchRequest {
        
        int DEFAULT_RESULT_LIMIT = 3;
        
        FetchRequest comingSchedules(long stationId);
        FetchRequest on(ServiceDay serviceDay);
        FetchRequest limitTo(int limit);
        void loadSchedules(AsyncTaskAdapter<List<Schedule>> adapter);
    }
}