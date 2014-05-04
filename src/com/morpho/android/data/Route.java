package com.morpho.android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Route implements JSONable, Serializable {

    private static final long serialVersionUID = -6167527654076731481L;

    private Long id;
    private Station station;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            name = src.optString("name", null);
            if (!src.isNull("station")) {
                station = new Station();
                station.fromJSON(src.getJSONObject("station").toString());
            }
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, station);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Route other = (Route) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (station == null) {
            if (other.station != null) return false;
        } else if (!station.equals(other.station)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append('{')
            .append("\"id\": ").append(id).append(", ")
            .append("\"name\": ").append('\"').append(name).append('\"').append(", ")
            .append("\"station\": ").append(station)
            .append('}')
            .toString();
    }
}