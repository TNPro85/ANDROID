package com.tnpro85.cathay;

import android.location.Location;
import android.os.Bundle;

import com.tnpro85.cathay.location.LocationController;
import com.tnpro85.cathay.uicontrols.LayoutLoading;
import com.tnpro85.cathay.utils.AlertUtils;


public class ActMain extends ActBase {

    LayoutLoading layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        // UI controls
        layoutLoading = (LayoutLoading) findViewById(R.id.layoutLoading);
        layoutLoading.setLoadingText(getString(R.string.str_loading_location));
        layoutLoading.show();

        // Action Bar
        setTitle(getString(R.string.app_name));

        if(LocationController.getInstance().isLocationEnable()) {
            LocationController.getInstance().getCurrentLocation(this);
//            Location location = LocationController.getInstance().getLastKnownLocation();
//            if(location != null)
//                AlertUtils.showMes("long: " + location.getLongitude() + "/ lat: " + location.getLatitude());
        }
        else
            AlertUtils.showMes("Location is not DISABLED");
    }
}
