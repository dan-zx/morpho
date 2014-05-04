package com.morpho.android.data;

public interface JSONable {

    String toJSON();
    void fromJSON(String json);
}
