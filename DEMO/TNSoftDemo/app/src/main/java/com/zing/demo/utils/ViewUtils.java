package com.zing.demo.utils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

public class ViewUtils {
    public static int dpToPx(Context context, float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    public static int spToPx(Context context, float sp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMsg(Context context, int strId) {
        Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
    }
}
