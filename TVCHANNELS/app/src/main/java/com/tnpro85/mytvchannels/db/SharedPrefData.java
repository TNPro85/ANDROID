package com.tnpro85.mytvchannels.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tnpro85.mytvchannels.application.MainApp;

public class SharedPrefData {
    private static final String appPrefName = "tvchannel";

    public static String getAppLanguage() {
        SharedPreferences pre = MainApp.getContext().getSharedPreferences(appPrefName, Context.MODE_PRIVATE);
        return pre.getString("appLang", "vn");
    }

    public static void setAppLanguage(String local) {
        SharedPreferences pre = MainApp.getContext().getSharedPreferences(appPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("appLang", TextUtils.isEmpty(local) ? "vn" : local);
        editor.commit();
    }
}
