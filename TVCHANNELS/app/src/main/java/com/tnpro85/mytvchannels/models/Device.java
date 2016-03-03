package com.tnpro85.mytvchannels.models;

import android.provider.BaseColumns;

public class Device implements BaseColumns {
    public int dId;
    public String dName, dDesc;
    public Device() {
        dName = "";
        dDesc = "";
    }

    public Device(int id, String name, String desc) {
        this();
        dId = id;
        dName = name;
        dDesc = desc;
    }
}
