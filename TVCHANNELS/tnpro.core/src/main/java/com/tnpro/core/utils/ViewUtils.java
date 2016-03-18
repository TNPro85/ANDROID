package com.tnpro.core.utils;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {
    public static int dpToPx(Context context, int dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    public static int spToPx(Context context, int sp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
