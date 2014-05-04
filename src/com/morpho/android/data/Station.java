package com.morpho.android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Station implements JSONable, Serializable {

    private static final long serialVersionUID = 7704847550005759269L;

    private Long id;
    private String name;
    private GeoArea area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoArea getArea() {
        return area;
    }

    public void setArea(GeoArea area) {
        this.area = area;
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
            if (!src.isNull("area")) {
                area = new GeoArea();
                area.fromJSON(src.getJSONObject("area").toString());
            }
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, area);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Station other = (Station) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (area == null) {
            if (other.area != null) return false;
        } else if (!area.equals(other.area)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append('{')
            .append("\"id\": ").append(id).append(", ")
            .append("\"name\": ").append('\"').append(name).append('\"').append(", ")
            .append("\"area\": ").append(area)
            .append('}')
            .toString();
    }
}