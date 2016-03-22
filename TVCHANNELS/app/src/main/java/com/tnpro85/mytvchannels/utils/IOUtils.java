package com.tnpro85.mytvchannels.utils;

import java.io.File;

public class IOUtils extends com.tnpro.core.utils.IOUtils {
    private static final String appFolderName = "/tvchannels/";
    public static String getStoragePath() {
        String extPath = getExternalStoragePath() + appFolderName;
        File file = new File(extPath);
        if(!file.exists())
            file.mkdir();

        return extPath;
    }

    public static String getBackupFolder() {
        String path = getStoragePath() + "backup/";
        File file = new File(path);
        if(!file.exists())
            file.mkdir();

        return path;
    }
}
