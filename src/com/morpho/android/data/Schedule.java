package com.morpho.android.data;

import android.content.Context;

import com.morpho.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Schedule implements JSONable, Serializable {

    private static final long serialVersionUID = -1744043002006668652L;

    public static enum ServiceDay {

        WEEKDAY, SATURDAY, SUNDAY, HOLYDAY;
        
        public String toString(Context context) {
            String[] arr = context.getResources().getStringArray(R.array.service_day);
            return arr[ordinal()];
        }
    }

    private Long id;
    private Route route;
    private Date departureAt;
    private ServiceDay serviceDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(Date departureAt) {
        this.departureAt = departureAt;
    }

    public ServiceDay getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(ServiceDay serviceDay) {
        this.serviceDay = serviceDay;
    }

    @Override
    public String toJSON() {
        return toString();
    }

    @Override
    public void fromJSON(String json) {
        try {
            JSONObject src = new JSONObject(json);
            id = src.isNull("id") ? null : src.getLong("id");
            if (!src.isNull("route")) {
                route = new Route();
                route.fromJSON(src.getJSONObject("route").toString());
            }
            departureAt = src.isNull("arriveAt") ? null : new Date(src.getLong("departureAt"));
            serviceDay = src.isNull("serviceDay") ? null : ServiceDay.valueOf(src.getString("serviceDay"));
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(id, route, departureAt, serviceDay);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Schedule other = (Schedule) obj;
        if (departureAt == null) {
            if (other.departureAt != null) return false;
        } else if (!departureAt.equals(other.departureAt)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (route == null) {
            if (other.route != null) return false;
        } else if (!route.equals(other.route)) return false;
        if (serviceDay != other.serviceDay) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append('{')
            .append("\"id\": ").append(id).append(", ")
            .append("\"route\": ").append(route).append(", ")
            .append("\"arriveAt\": ").append(departureAt == null ? null : departureAt.getTime()).append(", ")
            .append("\"serviceDay\": ").append(serviceDay)
            .append('}')
            .toString();
    }
}