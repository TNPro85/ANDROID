package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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
        getMenuInflater().inflate(R.menu.menu_act_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            try {
                Device device = checkAndAddDevice();
                if (device != null) {
                    Intent result = new Intent();
                    result.putExtra("device", device);
                    setResult(RESULT_OK, result);
                    finish();
                }
                else
                    Toast.makeText(ActDevice.this, "Data exists or invalid. Try again!", Toast.LENGTH_SHORT).show();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Toast.makeText(ActDevice.this, "Data exists or invalid. Try again!", Toast.LENGTH_SHORT).show();
            }
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

    private Device checkAndAddDevice() {
        etDeviceName.setError(null);
        etDeviceDesc.setError(null);

        String name = etDeviceName.getText().toString();
        String desc = etDeviceDesc.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etDeviceName.setError("Must not empty");
            etDeviceName.requestFocus();
            return null;
        } else if (TextUtils.isEmpty(desc)) {
            etDeviceDesc.setError("Must not empty");
            etDeviceDesc.requestFocus();
            return null;
        }

        Device device = new Device(name, desc);
        DBHelper.getInstance().addDevice(device);

        return device;
    }
}
