package com.tnpro85.mytvchannels.data;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.application.MainApp;

public class Const {
    public static final String APP_NAME = MainApp.getContext().getString(R.string.app_name);

    public static class REQCODE {
        public static final int ADD_DEVICE = 10000;
        public static final int EDIT_DEVICE = 10001;
        public static final int ADD_CHANNEL = 10002;
        public static final int EDIT_CHANNEL = 10003;
        public static final int COPY_CHANNEL = 10004;
        public static final int GO_SETTINGS = 10005;
    }

    public static class EXTRA {
        public static final String DEVICE = "dv";
        public static final String RESULT_TYPE = "rt";
        public static final String CHANNEL = "cn";
        public static final String COPYING_CHANNEL = "ccn";
    }

    public static class PERMISSION {
        public static final short READ_EXTERNAL_STORAGE = 100;
        public static final short WRITE_EXTERNAL_STORAGE = 101;
    }

    public static class JSON {
        public static final String DEVICE = "dvs";
        public static final String DEVICE_NAME = "n";
        public static final String DEVICE_DESC = "d";

        public static final String CHANNEL = "cns";
        public static final String CHANNEL_DEVICE = "dv";
        public static final String CHANNEL_NUM = "no";
        public static final String CHANNEL_NAME = "n";
        public static final String CHANNEL_DESC = "d";
    }
}
