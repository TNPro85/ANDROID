package com.tnpro85.mytvchannels.models;

import android.provider.BaseColumns;

public class Device implements BaseColumns{
    public String dId, dName, dDesc;
    public Device() {
        dId = "";
        dName = "";
        dDesc = "";
    }

    public Device(String id, String name, String desc) {
        dId = id;
        dName = name;
        dDesc = desc;
    }

    // DB IMPLEMENTATION
    public static class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "tb_devices";
        public static final String COLUMN_NAME_ID = "device_id";
        public static final String COLUMN_NAME_NAME = "device_name";
        public static final String COLUMN_NAME_DESC = "device_description";

        public static final String SQL_CREATE_TABLE = String.format(
                "CREATE TABLE %s (%s, %s, %s)",
                        TABLE_NAME,
                        COLUMN_NAME_ID,
                        COLUMN_NAME_NAME,
                        COLUMN_NAME_DESC
                );

        public static final String SQL_QUERY_ALL = String.format("SELECT * FROM %s", TABLE_NAME);
        public static final String SQL_DELETE_ITEM = "DELETE FROM %s WHERE device_id = ?";
        public static final String SQL_DELETE_ALL = String.format("DELETE * FROM %s", TABLE_NAME);
    }
}
