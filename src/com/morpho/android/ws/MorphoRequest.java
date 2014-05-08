package com.morpho.android.ws;

interface MorphoRequest {

    int NO_LIMIT = -1;
    
    MorphoRequest limitTo(int limit);
}
