package com.main.maomorn.tools.bottombar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/*
 * Basic Element of Bottom Bar Item
 */
class BottomBarItemBase {
    protected int iconResource;
    protected Drawable icon;
    protected int titleResource;
    protected String title;
    protected int color;

    /**
     * Get the drawabel of Bottom Bar Item
     * @param context context of activity that Bottom Bar is set
     * @return the drawable of the Bottom Bar Item
     */
    protected Drawable getIcon(Context context) {
        if (this.iconResource != 0) {
            return ContextCompat.getDrawable(context, this.iconResource);
        } else {
            return this.icon;
        }
    }

    /**
     * Get the title of Bottom Bar Item
     * @param context context of activity that Bottom Bar is set
     * @return the title text of the Bottom Bar Item
     */
    protected String getTitle(Context context) {
        if (this.titleResource != 0) {
            return context.getString(this.titleResource);
        } else {
            return this.title;
        }
    }
}
