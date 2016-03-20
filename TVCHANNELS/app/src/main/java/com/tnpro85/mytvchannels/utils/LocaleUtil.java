package com.tnpro85.mytvchannels.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tnpro85.mytvchannels.application.MainApp;
import com.tnpro85.mytvchannels.db.SharedPrefData;

import java.util.Locale;

public class LocaleUtil {
    public static void loadLocale(Context context) {
        String appLang = SharedPrefData.getAppLanguage();
        Locale myLocale = new Locale(appLang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;

        if(context == null)
            context = MainApp.getContext();

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void setLocale(Context context, String lang) {
        if(TextUtils.isEmpty(lang))
            lang = SharedPrefData.getAppLanguage();
        SharedPrefData.setAppLanguage(lang);

        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
