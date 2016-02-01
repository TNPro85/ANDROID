package com.tnpro85.mytvchannels.models;

import android.provider.BaseColumns;

public class Device implements BaseColumns {
    public String dName, dDesc;
    public Device() {
        dName = "";
        dDesc = "";
    }

    public Device(String name, String desc) {
        this();
        dName = name;
        dDesc = desc;
    }
}
