package com.tnpro85.cathay.models;

import android.location.Location;

public class LocationItem {
    public String mTitle, mDesc;
    public Location mLocation;

    public LocationItem() {
        mTitle = mDesc = "";
        mLocation = new Location("");
    }
}
