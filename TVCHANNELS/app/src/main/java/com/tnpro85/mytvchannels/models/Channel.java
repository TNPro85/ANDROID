package com.tnpro85.mytvchannels.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tnpro85.mytvchannels.data.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel implements Comparable<Channel>, Parcelable {

    public String cDevice;
    public int cNum;
    public String cName;
    public String cDesc;

    public Channel() {
        cName = "";
        cDesc = "";
    }

    public Channel(String device, int num, String name, String desc) {
        super();
        this.cDevice = device;
        this.cNum = num;
        this.cName = name;
        this.cDesc = desc;
    }

    protected Channel(Parcel in) {
        cDevice = in.readString();
        cNum = in.readInt();
        cName = in.readString();
        cDesc = in.readString();
    }

    public Channel(JSONObject json) {
        this();
        this.cDevice = json.optString(Const.JSON.CHANNEL_DEVICE);
        this.cNum = json.optInt(Const.JSON.CHANNEL_NUM);
        this.cName = json.optString(Const.JSON.CHANNEL_NAME);
        this.cDesc = json.optString(Const.JSON.CHANNEL_DESC);
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    @Override
    public int compareTo(@NonNull Channel another) {
        return ((Integer)this.cNum).compareTo(another.cNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cDevice);
        dest.writeInt(cNum);
        dest.writeString(cName);
        dest.writeString(cDesc);
    }

    public JSONObject toJSONObj() {
        JSONObject json = new JSONObject();
        try {
            json.put(Const.JSON.CHANNEL_DEVICE, cDevice);
            json.put(Const.JSON.CHANNEL_NUM, cNum);
            json.put(Const.JSON.CHANNEL_NAME, cName);
            json.put(Const.JSON.CHANNEL_DESC, cDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
