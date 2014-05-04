package com.morpho.android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GeoArea implements JSONable, Serializable {

    private static final long serialVersionUID = -7129733583451331658L;

    private GeoPoint location;
    private Float radius;

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    @Override
    public String toJSON() {
        return toString();
    }

    @Override
    public void fromJSON(String json) {
        try {
            JSONObject src = new JSONObject(json);
            if (!src.isNull("location")) {
                location = new GeoPoint();
                location.fromJSON(src.getJSONObject("location").toString());
            }
            radius = src.isNull("radius") ? null : (float) src.getDouble("radius");
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((radius == null) ? 0 : radius.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GeoArea other = (GeoArea) obj;
        if (location == null) {
            if (other.location != null) return false;
        } else if (!location.equals(other.location)) return false;
        if (radius == null) {
            if (other.radius != null) return false;
        } else if (!radius.equals(other.radius)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append('{')
            .append("\"location\": ").append(location).append(", ")
            .append("\"radius\": ").append(radius)
            .append('}')
            .toString();
    }
}