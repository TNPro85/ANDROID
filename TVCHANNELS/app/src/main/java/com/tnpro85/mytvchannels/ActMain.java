package com.tnpro85.mytvchannels;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tnpro85.mytvchannels.adapter.DeviceAdapter;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;


public class ActMain extends ActBase {

    private ArrayList<Device> lsDevices;
    private DeviceAdapter adapDevices;

    private ListView lvDevices;
    private FloatingActionButton fabAddDevice;
    private Snackbar sbError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
    }

    @Override
    protected void initUI(Bundle savedInstanceState) {
        setContentView(R.layout.act_main);

        vContainer = findViewById(R.id.container);

        lvDevices = (ListView) findViewById(R.id.lvDevices);

        fabAddDevice = (FloatingActionButton) findViewById(R.id.myFAB);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbError = Snackbar.make(vContainer, "Clicked", Snackbar.LENGTH_SHORT);
                sbError.show();
            }
        });

        super.initUI(savedInstanceState);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        adapDevices = new DeviceAdapter(this);
        lvDevices.setAdapter(adapDevices);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
