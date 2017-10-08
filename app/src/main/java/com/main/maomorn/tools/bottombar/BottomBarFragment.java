package com.main.maomorn.tools.bottombar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

@Deprecated
public class BottomBarFragment extends BottomBarItemBase {
    private Fragment fragment;

    /**
     * Creates a new Tab for the BottomBar.
     * @param fragment a Fragment to be shown when this Tab is selected.
     * @param iconResource a resource for the Tab icon.
     * @param title title for the Tab.
     */
    public BottomBarFragment(Fragment fragment, @DrawableRes int iconResource, @NonNull String title) {
        this.fragment = fragment;
        this.iconResource = iconResource;
        this.title = title;
    }

    /**
     * Creates a new Tab for the BottomBar.
     * @param fragment a Fragment to be shown when this Tab is selected.
     * @param icon an icon for the Tab.
     * @param title title for the Tab.
     */
    public BottomBarFragment(Fragment fragment, Drawable icon, @NonNull String title) {
        this.fragment = fragment;
        this.icon = icon;
        this.title = title;
    }

    /**
     * Creates a new Tab for the BottomBar.
     * @param fragment a Fragment to be shown when this Tab is selected.
     * @param icon an icon for the Tab.
     * @param titleResource resource for the title.
     */
    public BottomBarFragment(Fragment fragment, Drawable icon, @StringRes int titleResource) {
        this.fragment = fragment;
        this.icon = icon;
        this.titleResource = titleResource;
    }

    /**
     * Creates a new Tab for the BottomBar.
     * @param fragment a Fragment to be shown when this Tab is selected.
     * @param iconResource a resource for the Tab icon.
     * @param titleResource resource for the title.
     */
    public BottomBarFragment(Fragment fragment, @DrawableRes int iconResource, @StringRes int titleResource) {
        this.fragment = fragment;
        this.iconResource = iconResource;
        this.titleResource = titleResource;
    }

    protected Fragment getFragment() {
        return fragment;
    }
}
