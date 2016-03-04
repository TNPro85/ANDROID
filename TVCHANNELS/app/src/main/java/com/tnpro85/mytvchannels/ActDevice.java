package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Device;

public class ActDevice extends ActBase {

    EditText etDeviceName, etDeviceDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_device);

        etDeviceName = (EditText) findViewById(R.id.etDeviceName);
        etDeviceName.requestFocus();
        etDeviceDesc = (EditText) findViewById(R.id.etDeviceDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            try {
                Device device = new Device(etDeviceName.getEditableText().toString(), etDeviceDesc.getEditableText().toString());
                DBHelper.getInstance().addDevice(device);

                Intent result = new Intent();
                result.putExtra("device", device);
                setResult(RESULT_OK, result);
            } catch (SQLiteConstraintException e) {
                setResult(RESULT_CANCELED);
                e.printStackTrace();
            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
