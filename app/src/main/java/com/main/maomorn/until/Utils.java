package com.main.maomorn.until;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.MenuRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.main.maomorn.tools.bottombar.BottomBarTab;

/**
 * Created by MaoMorn on 2017/3/5.
 */

public class Utils {

    /**
     * Converts dps to pixels nicely.
     *
     * @param context the Context for getting the resources
     * @param dp      dimension in dps
     * @return dimension in pixels
     */
    public static int dpToPixel(Context context, float dp) {
        if (dp < 0) {
            return 0;
        }
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5);
    }

    /**
     * Converts sps to pixels nicely
     *
     * @param context the Context for getting the resources
     * @param sp      dimension in sps
     * @return dimension in pixels
     */
    public static int spToPixel(Context context, float sp) {
        if (sp < 0) {
            return 0;
        }
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (sp * metrics.scaledDensity + 0.5);
    }

}
