package com.tnpro85.mytvchannels;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tnpro.core.utils.VersionUtils;
import com.tnpro85.mytvchannels.application.MainApp;
import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.CP;
import com.tnpro85.mytvchannels.db.DBConst;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.db.SharedPrefData;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.utils.IOUtils;
import com.tnpro85.mytvchannels.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActSetting extends ActBase {

    private AppCompatSpinner spLanguage;
    private ArrayAdapter<String> arrayAdapter;
    private String arrLangKey[], arrLang[], curLang;
    private TextView tvLastBackup;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_settings);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_setting));

        spLanguage = (AppCompatSpinner) findViewById(R.id.spLanguage);
        findViewById(R.id.vBackup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VersionUtils.hasMarshmallow() && ContextCompat.checkSelfPermission(ActSetting.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Check if needed to show users the reason why we need this permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ActSetting.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(ActSetting.this)
                                .setTitle(getString(R.string.permission_request))
                                .setMessage(getString(R.string.permission_write_storage))
                                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(ActSetting.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                Const.PERMISSION.WRITE_EXTERNAL_STORAGE);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(ActSetting.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Const.PERMISSION.WRITE_EXTERNAL_STORAGE);
                    }
                    return;
                }

                // If permission is granted, do the backup
                doBackupToSdcard();
            }
        });

        findViewById(R.id.vRestore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VersionUtils.hasMarshmallow() && ContextCompat.checkSelfPermission(ActSetting.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ActSetting.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(ActSetting.this)
                                .setTitle(getString(R.string.permission_request))
                                .setMessage(getString(R.string.permission_read_storage))
                                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(ActSetting.this,
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                Const.PERMISSION.READ_EXTERNAL_STORAGE);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(ActSetting.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Const.PERMISSION.READ_EXTERNAL_STORAGE);
                    }
                    return;
                }

                // If permission is granted, do the restoration
                doRestoreData();
            }
        });

        findViewById(R.id.vAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActSetting.this, "TNpro85", Toast.LENGTH_SHORT).show();
            }
        });

        tvLastBackup = (TextView) findViewById(R.id.tvLastBackup);
        updateBackupTime();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        setupSettings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERMISSION.READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doRestoreData();
                }

                break;
            case Const.PERMISSION.WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doBackupToSdcard();
                }
                break;
        }
    }

    private void setupSettings() {
        arrLangKey = getResources().getStringArray(R.array.arr_lang_key);
        arrLang = getResources().getStringArray(R.array.arr_lang);
        arrayAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, arrLang);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spLanguage.setAdapter(arrayAdapter);
        curLang = SharedPrefData.getAppLanguage();
        for(int i = 0; i < arrLangKey.length; i++) {
            if(curLang.equals(arrLangKey[i]))
                spLanguage.setSelection(i);
        }

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPrefData.setAppLanguage(arrLangKey[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void doBackupToSdcard() {
        showLoadingDlg(R.string.str_doing, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                try {
                    JSONObject json = new JSONObject();
                    JSONArray devices = new JSONArray();
                    JSONArray channels = new JSONArray();
                    ArrayList<Device> arrDevice = DBHelper.getInstance().getAllDevices();
                    for(int i = 0; i < arrDevice.size(); i++) {
                        Device item = arrDevice.get(i);
                        devices.put(item.toJSONObj());
                    }
                    json.put(Const.JSON.DEVICE, devices);

                    ArrayList<Channel> arrChannel = DBHelper.getInstance().getAllChannel(null);
                    for(int i = 0; i < arrChannel.size(); i++) {
                        Channel item = arrChannel.get(i);
                        channels.put(item.toJSONObj());
                    }
                    json.put(Const.JSON.CHANNEL, channels);

                    String path = Utils.getBackupPath();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                    bos.write(json.toString().getBytes());
                    bos.flush();
                    bos.close();

                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hideLoadingDlg();
                    final String msg = result ? getString(R.string.str_backup_success) : getString(R.string.str_error_general);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActSetting.this, msg, Toast.LENGTH_SHORT).show();
                            updateBackupTime();
                        }
                    });
                }
            }
        }).start();
    }

    private void doRestoreData() {
        final String path = Utils.getBackupPath();
        final File backupFile = new File(path);
        if(!backupFile.exists()) {
            Toast.makeText(ActSetting.this, getString(R.string.str_backup_notfound), Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(ActSetting.this)
                .setTitle(R.string.str_confirm)
                .setMessage(R.string.str_restore_confirm)
                .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoadingDlg(R.string.str_doing, false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean result = false;
                                try {
                                    String value = IOUtils.readStringFromFile(path);
                                    if (!TextUtils.isEmpty(value)) {
                                        JSONObject json = new JSONObject(value);
                                        result = restoreToDB(json);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    hideLoadingDlg();
                                    final String msg = result ? getString(R.string.str_restore_success) : getString(R.string.str_error_general);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showToast(ActSetting.this, msg);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }).show();
    }

    private boolean restoreToDB(JSONObject json) {
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ContentProviderOperation deleteAllDeviceOp = ContentProviderOperation.newDelete(CP.CONTENT_URI_DEVICES).build();
            ops.add(deleteAllDeviceOp);
            ContentProviderOperation deleteAllChannelOp = ContentProviderOperation.newDelete(CP.CONTENT_URI_CHANNELS).build();
            ops.add(deleteAllChannelOp);

            JSONArray arrDevices = json.getJSONArray(Const.JSON.DEVICE);
            if(arrDevices != null && arrDevices.length() > 0) {
                for(int i = 0; i < arrDevices.length(); i++) {
                    JSONObject obj = arrDevices.getJSONObject(i);
                    Device device = new Device(obj);
                    ContentValues values = new ContentValues();
                    values.put(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME, device.dName);
                    values.put(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_DESC, device.dDesc);
                    ContentProviderOperation addDeviceOp = ContentProviderOperation.newInsert(CP.CONTENT_URI_DEVICES)
                            .withValues(values).build();
                    ops.add(addDeviceOp);
                }
            }

            JSONArray arrChannels = json.getJSONArray(Const.JSON.CHANNEL);
            if(arrChannels != null && arrChannels.length() > 0) {
                for(int i = 0; i < arrChannels.length(); i++) {
                    JSONObject obj = arrChannels.getJSONObject(i);
                    Channel channel = new Channel(obj);
                    ContentValues values = new ContentValues();
                    values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE, channel.cDevice);
                    values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM, channel.cNum);
                    values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNAME, channel.cName);
                    values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDESC, channel.cDesc);
                    ContentProviderOperation addChannelOp = ContentProviderOperation.newInsert(CP.CONTENT_URI_CHANNELS)
                            .withValues(values).build();
                    ops.add(addChannelOp);
                }
            }

            MainApp.getContext().getContentResolver().applyBatch(CP.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void updateBackupTime() {
        String path = Utils.getBackupPath();
        File backupFile = new File(path);

        Locale locale = new Locale(SharedPrefData.getAppLanguage());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);

        if(tvLastBackup != null) {
            String time = getString(R.string.str_backup_latest)
                    + " "
                    + (backupFile.exists()? df.format(new Date(backupFile.lastModified())) : getString(R.string.str_backup_notfound)) ;
            tvLastBackup.setText(time);
        }
    }
}
