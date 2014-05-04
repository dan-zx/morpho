/*
 * Copyright 2014 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpho.android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * GeoPoint represents a geographic position in the geographic coordinate
 * system.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class GeoPoint implements JSONable, Serializable {

    private static final long serialVersionUID = -2333340992671095314L;
    private static final int MAXIMUM_FRACTION_DIGITS_IN_COORDINATES = 12;
    private static final NumberFormat VALID_COORDINATES_FORMAT;

    static {
        VALID_COORDINATES_FORMAT = NumberFormat.getInstance(Locale.ENGLISH);
        VALID_COORDINATES_FORMAT.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS_IN_COORDINATES);
    }

    private Double latitude;
    private Double longitude;

    /** @param latitude the latitude coordinate. */
    public void setLatitude(Double latitude) {
        this.latitude = toValidCoordinate(latitude);
    }

    /** @return the latitude coordinate. */
    public Double getLatitude() {
        return latitude;
    }

    /** @param longitude the longitude coordinate. */
    public void setLongitude(Double longitude) {
        this.longitude = toValidCoordinate(longitude);
    }

    /** @return the longitude coordinate. */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Corrects a coordinate point if its wrong.
     *
     * @param coordinate a coordinate point.
     * @return a valid coordinate point.
     */
    private Double toValidCoordinate(Double coordinate) {
        return coordinate != null ? Double.parseDouble(VALID_COORDINATES_FORMAT.format(coordinate)) : null;
    }

    /** {@inheritDoc} */
    @Override
    public void fromJSON(String json) {
        try {
            JSONObject src = new JSONObject(json);
            latitude = src.isNull("latitude") ? null : src.getDouble("latitude");
            longitude = src.isNull("longitude") ? null : src.getDouble("longitude");
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toJSON() {
        return toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final GeoPoint other = (GeoPoint) obj;
        if ((latitude == null) ? (other.latitude != null) : !latitude.equals(other.latitude)) return false;
        if ((longitude == null) ? (other.longitude != null) : !longitude.equals(other.longitude)) return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new StringBuilder()
                .append('{')
                .append("\"latitude\": ").append(latitude).append(", ")
                .append("\"longitude\": ").append(longitude)
                .append('}')
                .toString();
    }
}