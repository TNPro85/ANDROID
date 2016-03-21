package com.tnpro85.mytvchannels;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Device;

public class ActDevice extends ActBase {

    public static final String RESULT_ADD = "added", RESULT_UPDATE = "updated";

    private Device mDeviceToEdit;
    EditText etDeviceName, etDeviceDesc;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_device);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_device));

        etDeviceName = (EditText) findViewById(R.id.etDeviceName);
        etDeviceName.requestFocus();
        etDeviceDesc = (EditText) findViewById(R.id.etDeviceDesc);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if(data != null) {
            mDeviceToEdit = data.getParcelable(Const.EXTRA.DEVICE);
            if(mDeviceToEdit != null) {
                etDeviceName.setText(mDeviceToEdit.dName);
                etDeviceName.setSelection(mDeviceToEdit.dName.length());
                etDeviceDesc.setText(mDeviceToEdit.dDesc);
            }
        }
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
                etDeviceName.setError(null);
                etDeviceDesc.setError(null);

                final String name = etDeviceName.getText().toString();
                final String desc = etDeviceDesc.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    etDeviceName.setError("Must not empty");
                    etDeviceName.requestFocus();
                    return true;
                } else if (TextUtils.isEmpty(desc)) {
                    etDeviceDesc.setError("Must not empty");
                    etDeviceDesc.requestFocus();
                    return true;
                }

                if(DBHelper.getInstance().getDevice(name) != null) {
                    new AlertDialog.Builder(ActDevice.this)
                            .setTitle(getString(R.string.str_confirm))
                            .setMessage("Device exists. Do you want to update it?")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    addDevice(new Device(name, desc), RESULT_UPDATE);
                                }
                            })
                            .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                else if(mDeviceToEdit != null) {
                        new AlertDialog.Builder(ActDevice.this)
                                .setTitle(getString(R.string.str_confirm))
                                .setMessage("This device does not exist. Do you want to add it?")
                                .setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        addDevice(new Device(name, desc), RESULT_ADD);
                                    }
                                })
                                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                }
                else
                    addDevice(new Device(name, desc), null);
            }
            catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Toast.makeText(ActDevice.this, "Invalid data. Try again!", Toast.LENGTH_SHORT).show();
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

    private void addDevice(Device device, String resultType) {
        DBHelper.getInstance().addDevice(device);
        Intent result = new Intent();
        if(!TextUtils.isEmpty(resultType))
            result.putExtra(Const.EXTRA.RESULT_TYPE, resultType);
        result.putExtra(Const.EXTRA.DEVICE, device);
        setResult(RESULT_OK, result);
        finish();
    }
}
