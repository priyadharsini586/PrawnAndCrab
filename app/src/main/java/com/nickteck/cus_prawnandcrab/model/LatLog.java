package com.nickteck.cus_prawnandcrab.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by admin on 5/26/2018.
 */

public class LatLog {

    private Float LatLagDistance;
    private LatLng latLng;

    public LatLog(Float latLagDistance, LatLng latLng) {
        LatLagDistance = latLagDistance;
        this.latLng = latLng;
    }

    public Float getLatLagDistance() {
        return LatLagDistance;
    }

    public void setLatLagDistance(Float latLagDistance) {
        LatLagDistance = latLagDistance;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
