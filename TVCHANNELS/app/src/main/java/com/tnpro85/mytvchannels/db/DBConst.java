package com.tnpro85.mytvchannels.db;

import android.provider.BaseColumns;

public class DBConst {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "TvChannels.db";

    public static final class TABLE {
        public static final String TBL_DEVICE_NAME = "tb_devices";
        public static class TBL_DEVICE_COL implements BaseColumns {
            public static final String COLUMN_NAME_ID = "device_id";
            public static final String COLUMN_NAME_NAME = "device_name";
            public static final String COLUMN_NAME_DESC = "device_description";
        }
    }
}
