package com.tnpro85.mytvchannels.data;

import com.tnpro85.mytvchannels.R;
import com.tnpro85.mytvchannels.application.MainApp;

public class Const {
    public static final String APP_NAME = MainApp.getContext().getString(R.string.app_name);

    public static class REQCODE {
        public static final int ADD_DEVICE = 10000;
        public static final int ADD_CHANNEL = 10001;
    }
}
