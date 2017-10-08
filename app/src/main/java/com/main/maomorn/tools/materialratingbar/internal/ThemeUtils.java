package com.main.maomorn.tools.materialratingbar.internal;

import android.content.Context;
import android.content.res.TypedArray;

public class ThemeUtils {

    private ThemeUtils() {}

    public static int getColorFromAttrRes(int attr, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[] {attr});
        try {
            return typedArray.getColor(0, 0);
        } finally {
            typedArray.recycle();
        }
    }

    public static float getFloatFromAttrRes(int attrRes, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[] {attrRes});
        try {
            return typedArray.getFloat(0, 0);
        } finally {
            typedArray.recycle();
        }
    }
}
