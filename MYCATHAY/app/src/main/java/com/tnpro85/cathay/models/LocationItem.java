package com.tnpro85.cathay.models;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationItem {
    public String mTitle, mDesc;
    public Location mLocation;

    public LocationItem() {
        mTitle = mDesc = "";
        mLocation = new Location("");
    }

    public LocationItem(JSONObject json) {
        this();
        try {
            mTitle = json.getString("name");
            mDesc = json.getString("address");
            mLocation.setLongitude(json.getDouble("long"));
            mLocation.setLatitude(json.getDouble("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
