package com.tnpro85.mytvchannels;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.db.SharedPrefData;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActSetting extends ActBase {

    private AppCompatSpinner spLanguage;
    private ArrayAdapter<String> arrayAdapter;
    private String arrLangKey[], arrLang[], curLang;

    private View vAbout, vBackup;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_settings);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_setting));

        spLanguage = (AppCompatSpinner) findViewById(R.id.spLanguage);
        vBackup = findViewById(R.id.vBackup);
        vBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActSetting.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ActSetting.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(ActSetting.this, "read request", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(ActSetting.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                Const.PERMISSION.READ_EXTERNAL_STORAGE);
                    }
                    return;
                }

                if(ContextCompat.checkSelfPermission(ActSetting.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ActSetting.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(ActSetting.this, "write request", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ActivityCompat.requestPermissions(ActSetting.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                Const.PERMISSION.WRITE_EXTERNAL_STORAGE);
                    }
                    return;
                }

                try {
                    JSONObject json = new JSONObject();
                    JSONArray devices = new JSONArray();
                    JSONArray channels = new JSONArray();
                    ArrayList<Device> arrDevice = DBHelper.getInstance().getAllDevices();
                    for(int i = 0; i < arrDevice.size(); i++) {
                        Device item = arrDevice.get(i);
                        devices.put(item.toJSONObj());
                    }
                    json.put("devices", devices);

                    ArrayList<Channel> arrChannel = DBHelper.getInstance().getAllChannel(null);
                    for(int i = 0; i < arrChannel.size(); i++) {
                        Channel item = arrChannel.get(i);
                        channels.put(item.toJSONObj());
                    }
                    json.put("channels", channels);

                    Toast.makeText(ActSetting.this, json.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        vAbout = findViewById(R.id.vAbout);
        vAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActSetting.this, "TNpro85", Toast.LENGTH_SHORT).show();
            }
        });
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
                    // permission was granted!
                } else {
                    // permission denied!
                }

                break;
            case Const.PERMISSION.WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted!
                } else {
                    // permission denied!
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
