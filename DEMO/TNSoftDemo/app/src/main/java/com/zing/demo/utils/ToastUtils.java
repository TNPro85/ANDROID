package com.zing.demo.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context, String toastStr) {
        Toast.makeText(context, toastStr, Toast.LENGTH_LONG).show();
    }
}
