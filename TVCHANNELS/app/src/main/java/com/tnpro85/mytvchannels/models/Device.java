package com.tnpro85.mytvchannels.models;

/**
 * Created by TUAN on 20/05/2015.
 */
public abstract class Device {
    public String dName, dDesc;

    public Device(String name, String description) {
        dName = name;
        dDesc = description;
    }
}
