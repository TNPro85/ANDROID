package com.tnpro85.cathay.location;

import android.location.Location;

/**
 * Created by TUAN on 14/06/2015.
 */
public interface LocationEventListener {
    void onLocationChanged(Location location);
    void onGetLocationFailed();
}
