package com.tnpro.core.utils;

import android.os.Build;

public class VersionUtils {
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
