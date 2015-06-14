package com.tnpro85.cathay;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.LocationListener;
import com.tnpro85.cathay.location.LocationController;
import com.tnpro85.cathay.models.LocationItem;
import com.tnpro85.cathay.uicontrols.LayoutLoading;
import com.tnpro85.cathay.utils.AlertUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;


public class ActMain extends ActBase {

    LayoutLoading layoutLoading;
    ListView lvLocation;

    LocationAdapter mLocationAdapter;
    ArrayList<LocationItem> locationList = new ArrayList<>();
    ArrayList<LocationItem> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        // UI controls
        lvLocation = (ListView) findViewById(R.id.lvLocation);
        layoutLoading = (LayoutLoading) findViewById(R.id.layoutLoading);

        // Action Bar
        setTitle(getString(R.string.app_name));

        // Data
        mLocationAdapter = new LocationAdapter(this);
        lvLocation.setAdapter(mLocationAdapter);
        lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationItem item = filterList.get(position);
                openGoogleMapForDirection(ActMain.this, item.mTitle, item.mLocation);
            }
        });
        loadData();
    }

    private void loadData() {
        layoutLoading.setLoadingText(getString(R.string.str_loading_data));
        layoutLoading.show();

        final StringBuilder sb = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("data.txt"), "UTF-8"));
                    String data;

                    while(!TextUtils.isEmpty(data = reader.readLine())) {
                        sb.append(data);
                    }

                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String result = sb.toString();
                    JSONArray arr = new JSONArray(result);
                    for(int i = 0; i < arr.length(); i++) {
                        JSONObject js = arr.getJSONObject(i);
                        LocationItem li = new LocationItem(js);
                        locationList.add(li);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(locationList.size() > 0) {
                                layoutLoading.setLoadingText(getString(R.string.str_loading_data));
                                loadLocation();
                            }
                            else {
                                layoutLoading.hide();
                                AlertUtils.showMes("INVALID DATA");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void loadLocation() {
        try {
            if(LocationController.getInstance().isLocationEnable()) {
                LocationController.getInstance().getCurrentLocation(new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        LocationController.getInstance().closeGoogleApiClient();
                        updateLocation(location);
                    }
                });
            }
            else
                AlertUtils.showMes("Location is not DISABLED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLocation(Location location) {
        if(location == null)
            location = LocationController.getInstance().getLastKnownLocation();

        if(location != null) {
            AlertUtils.showMes("long: " + location.getLongitude() + "/ lat: " + location.getLatitude());
            layoutLoading.hide();

            filterList = new ArrayList<>(locationList);
            Iterator<LocationItem> iterator = filterList.iterator();
            while(iterator.hasNext()) {
                LocationItem item = iterator.next();
                if(location.distanceTo(item.mLocation) / 1000 > 10)
                    iterator.remove();
            }

            mLocationAdapter.setData(filterList);
            mLocationAdapter.notifyDataSetChanged();
        }
        else
            AlertUtils.showMes("Cannot get location!!!");
    }

    public static void openGoogleMapApp(Context context, String address, Location location) {
        if (context != null) {
            try {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                if(isGoogleMapsInstalled(context)) {
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=18&q=" + latitude + "," + longitude + "(" + address + ")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
                else {
                    String query = address + "@"+ latitude + "," + longitude;
                    String encodedQuery = Uri.encode(query);
                    String url = "https://maps.google.com/maps?z=16&q="+encodedQuery+"&t=m";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openGoogleMapForDirection(Context context, String address, Location location) {
        if (context != null) {
            try {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                if(isGoogleMapsInstalled(context)) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&z=18&f=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
                else {
                    String query = address + "@"+ latitude + "," + longitude;
                    String encodedQuery = Uri.encode(query);
                    String url = "https://maps.google.com/maps?z=16&q="+encodedQuery+"&t=m";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isGoogleMapsInstalled(Context context)
    {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            if(info != null)
                return true;

            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
}
