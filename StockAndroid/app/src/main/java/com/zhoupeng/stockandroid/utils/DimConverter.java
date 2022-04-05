package com.zhoupeng.stockandroid.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimConverter {
    public static int Dp2Pix(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int Pix2Dp(float px, Context context) {
        return (int) (px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
