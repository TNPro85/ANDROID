package com.tnpro85.cathay.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tnpro85.cathay.application.MainApplication;
import com.tnpro85.cathay.utils.AlertUtils;

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

    private LocationListener mLocationListener;
    private GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(MainApplication.getAppContext())
            .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    getLocation();
                }

                @Override
                public void onConnectionSuspended(int i) {
                    AlertUtils.showMes("Cannot to Google Location Service has been suspended.");
                }
            })
            .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    AlertUtils.showMes("Cannot connect to Google Location Service.");
                }
            })
            .addApi(LocationServices.API)
            .build();

    public Location getCurrentLocation(LocationListener locationListener) {
        mLocationListener = locationListener;
        if(mGoogleApiClient.isConnected())
            getLocation();
        else
            mGoogleApiClient.connect();
        return null;
    }

    public void closeGoogleApiClient() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    private void getLocation() {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
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
