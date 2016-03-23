package com.tnpro85.mytvchannels.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.tnpro85.mytvchannels.data.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class Device implements Parcelable {
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

    protected Device(Parcel in) {
        dName = in.readString();
        dDesc = in.readString();
    }

    public Device(JSONObject json) {
        this();
        dName = json.optString(Const.JSON.DEVICE_NAME);
        dDesc = json.optString(Const.JSON.DEVICE_DESC);
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dName);
        dest.writeString(dDesc);
    }

    public JSONObject toJSONObj() {
        JSONObject json = new JSONObject();
        try {
            json.put(Const.JSON.DEVICE_NAME, dName);
            json.put(Const.JSON.DEVICE_DESC, dDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
