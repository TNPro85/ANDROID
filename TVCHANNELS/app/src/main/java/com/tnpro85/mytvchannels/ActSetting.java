package com.tnpro85.mytvchannels;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tnpro85.mytvchannels.db.SharedPrefData;

public class ActSetting extends ActBase {

    private AppCompatSpinner spLanguage;
    private ArrayAdapter<String> arrayAdapter;
    private String arrLangKey[], arrLang[], curLang;

    private View vAbout;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_settings);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_setting));

        spLanguage = (AppCompatSpinner) findViewById(R.id.spLanguage);
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
