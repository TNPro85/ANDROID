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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.utils.Utils;

public class ActDevice extends ActBase {

    public static final String RESULT_ADD = "added", RESULT_UPDATE = "updated";

    private Device mDeviceToEdit;
    private EditText etDeviceName, etDeviceDesc;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_device);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_device));

        etDeviceName = (EditText) findViewById(R.id.etDeviceName);
        etDeviceDesc = (EditText) findViewById(R.id.etDeviceDesc);
        etDeviceDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        onActionDone();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if(data != null) {
            mDeviceToEdit = data.getParcelable(Const.EXTRA.DEVICE);
            if(mDeviceToEdit != null) {
                etDeviceName.requestFocus();
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
            onActionDone();
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

    private void onActionDone() {
        try {
            etDeviceName.setError(null);
            etDeviceDesc.setError(null);

            final String name = etDeviceName.getText().toString();
            final String desc = etDeviceDesc.getText().toString();

            if (TextUtils.isEmpty(name)) {
                etDeviceName.setError(getString(R.string.str_error_must_not_empty));
                etDeviceName.requestFocus();
                return;
            } else if (TextUtils.isEmpty(desc)) {
                etDeviceDesc.setError(getString(R.string.str_error_must_not_empty));
                etDeviceDesc.requestFocus();
                return;
            }

            if(DBHelper.getInstance().getDevice(name) != null) {
                new AlertDialog.Builder(ActDevice.this)
                        .setTitle(R.string.str_confirm)
                        .setMessage(R.string.str_device_update_confirm)
                        .setPositiveButton(R.string.str_update, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                addDevice(new Device(name, desc), RESULT_UPDATE);
                            }
                        })
                        .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
            else if(mDeviceToEdit != null) {
                new AlertDialog.Builder(ActDevice.this)
                        .setTitle(getString(R.string.str_confirm))
                        .setMessage(R.string.str_device_add_confirm)
                        .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
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
            Utils.showMsg(ActDevice.this, R.string.str_error_invalid_data);
        }
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
