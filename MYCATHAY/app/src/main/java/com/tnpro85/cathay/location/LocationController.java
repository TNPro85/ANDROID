package com.tnpro85.cathay.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tnpro85.cathay.application.MainApplication;

import java.util.List;

public class LocationController {

    private static LocationManager locationManager;
    private static volatile LocationController mLocationController;
    public LocationController() {}

    public static LocationController getInstance() {
        if(mLocationController == null) {
            synchronized (LocationController.class) {
                if(mLocationController == null) {
                    mLocationController = new LocationController();
                }
            }
        }

        if(locationManager == null)
            locationManager = (LocationManager) MainApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);

        return mLocationController;
    }

    public Location getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest,  );
        return null;
    }

    public Location getLastKnownLocation() {
        Location result = null;
        List<String> providers = locationManager.getProviders(true);
        for (int i = 0; i < providers.size(); i++) {
            result = locationManager.getLastKnownLocation(providers.get(i));
            if (result != null)
                break;
        }

        return result;
    }

    public boolean isLocationEnable() {
        boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabledNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return enabledGPS && enabledGPS;
    }
}
