package com.zing.demo.utils;

import android.os.Environment;

import java.io.File;

public class IOUtils {
    public static String getExternalSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static boolean checkFolderOrFileExist(String path) {
        File fileToCheck = new File(path);
        return fileToCheck.exists();
    }
}
