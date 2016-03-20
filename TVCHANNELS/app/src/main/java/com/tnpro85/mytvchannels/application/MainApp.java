package com.tnpro85.mytvchannels.application;

import android.app.Application;
import android.content.Context;

import com.tnpro85.mytvchannels.utils.LocaleUtil;

public class MainApp extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LocaleUtil.loadLocale(mContext);
    }
}
