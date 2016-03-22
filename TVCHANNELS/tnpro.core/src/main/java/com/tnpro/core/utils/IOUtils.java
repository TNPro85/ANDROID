package com.tnpro.core.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class IOUtils {
    public static String getExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String readStringFromFile(String path) {
        String result = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            fis.close();

            result = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
