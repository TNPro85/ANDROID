package com.tnpro85.mytvchannels.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static String getBackupPath() {
        return IOUtils.getBackupFolder() + "backup.dat";
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
