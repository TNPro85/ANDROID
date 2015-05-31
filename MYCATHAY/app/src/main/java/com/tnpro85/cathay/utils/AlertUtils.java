package com.tnpro85.cathay.utils;

import android.widget.Toast;

import com.tnpro85.cathay.application.MainApplication;

public class AlertUtils {

    public static void showMes(String mes) {
        Toast.makeText(MainApplication.getAppContext(), mes, Toast.LENGTH_SHORT).show();
    }
}
