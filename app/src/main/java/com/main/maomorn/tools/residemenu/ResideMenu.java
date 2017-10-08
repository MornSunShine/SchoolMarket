package com.main.maomorn.tools.residemenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.main.maomorn.schoolmarket.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaoMorn on 2017-04-18.
 */

public class ResideMenu extends FrameLayout {

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZONTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    private LinearLayout layoutLeftMenu;
    private LinearLayout layoutRightMenu;
    private View scrollViewLeftMenu;
    private View scrollViewRightMenu;
    private View scrollViewMenu;
    /**
     * Current attaching activity.
     */
    private Activity activity;
    /**
     * The DecorView of current activity.
     */
    private ViewGroup viewDecor;
    private TouchDisableView viewActivity;
    /**
     * The flag of menu opening status.
     */
    private boolean isOpened;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * Views which need stop to intercept touch events.
     */
    private List<View> ignoredViews;
    private List<ResideMenuItem> leftMenuItems;
    private List<ResideMenuItem> rightMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // Valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue = 0.5f;

    private boolean mUse3D;
    private static final int ROTATE_Y_ANGLE = 10;

    /**
     * This constructor uses default menu layouts to generate
     * Reside Menu
     * @param context the context of the attached activity
     */
    public ResideMenu(Context context) {
        super(context);
        initViews(context, -1, -1);
    }

    /**
     * This constructor provides you to create menus with your own custom
     * layouts, but if you use custom menu then do not call addMenuItem because
     * it will not be able to find default views
     * @param context the context of the attached activity
     * @param customLeftMenuId the id of left menu' layout
     * @param customRightMenuId the id of right menu layout
     */
    public ResideMenu(Context context, int customLeftMenuId,
                      int customRightMenuId) {
        super(context);
        initViews(context, customLeftMenuId, customRightMenuId);
    }

    private void initViews(Context context, int customLeftMenuId,
                           int customRightMenuId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tools_residemenu_custom, this);

        if (customLeftMenuId >= 0) {
            scrollViewLeftMenu = inflater.inflate(customLeftMenuId, this, false);
        } else {
            scrollViewLeftMenu = inflater.inflate(
                    R.layout.tools_residemenu_custom_left_scrollview, this, false);
            layoutLeftMenu = (LinearLayout) scrollViewLeftMenu.findViewById(R.id.layout_left_menu);
        }

        if (customRightMenuId >= 0) {
            scrollViewRightMenu = inflater.inflate(customRightMenuId, this, false);
        } else {
            scrollViewRightMenu = inflater.inflate(
                    R.layout.tools_residemenu_custom_right_scrollview, this, false);
            layoutRightMenu = (LinearLayout) scrollViewRightMenu.findViewById(R.id.layout_right_menu);
        }

        imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
        imageViewBackground = (ImageView) findViewById(R.id.iv_background);

        RelativeLayout menuHolder = (RelativeLayout) findViewById(R.id.sv_menu_holder);
        menuHolder.addView(scrollViewLeftMenu);
        menuHolder.addView(scrollViewRightMenu);
    }

    /**
     * Returns left menu view so you can findViews and do whatever you want with
     */
    public View getLeftMenuView() {
        return scrollViewLeftMenu;
    }

    /**
     * Returns right menu view so you can findViews and do whatever you want with
     */
    public View getRightMenuView() {
        return scrollViewRightMenu;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        // Applies the content insets to the view's padding, consuming that
        // content (modifying the insets to be 0),
        // and returning true. This behavior is off by default and can be
        // enabled through setFitsSystemWindows(boolean)
        // in api14+ devices.

        // This is added to fix soft navigationBar's overlapping to content above LOLLIPOP
        int bottomPadding = viewActivity.getPaddingBottom() + insets.bottom;
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasBackKey || !hasHomeKey) {//there's a navigation bar
            bottomPadding += getNavigationBarHeight();
        }

        this.setPadding(viewActivity.getPaddingLeft() + insets.left,
                viewActivity.getPaddingTop() + insets.top,
                viewActivity.getPaddingRight() + insets.right,
                bottomPadding);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }

    /**
     * get the height of the Navigation Bar
     * @return height of navigation bar in pixel
     */
    private int getNavigationBarHeight() {
        Resources resources = getResources();
        /**
         * get the resource id of navigation bar's height
         * 'navigation_bar_height' : name of resource
         * 'dimen' : type of resource
         * 'android' : name of package
         */
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Set up the activity;
     * @param activity the Activity object attached to
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
    }

    /**
     * Initiative some basic value
     * @param activity the Activity object attached to
     */
    private void initValue(Activity activity) {
        this.activity = activity;
        leftMenuItems = new ArrayList<ResideMenuItem>();
        rightMenuItems = new ArrayList<ResideMenuItem>();
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);

        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);

        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
        parent.removeView(scrollViewLeftMenu);
        parent.removeView(scrollViewRightMenu);
    }

    /**
     * set the x,y scale of shadow by orientation of the phone's widow
     */
    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * Set the background image of menu;
     * @param imageResource
     */
    public void setBackground(int imageResource) {
        imageViewBackground.setImageResource(imageResource);
    }

    /**
     * The visibility of the shadow under the activity;
     * @param isVisible true as visible, false as NULL background
     */
    public void setShadowVisible(boolean isVisible) {
        if (isVisible)
            imageViewShadow.setBackgroundResource(R.drawable.shadow);
        else
            imageViewShadow.setBackgroundResource(0);
    }

    /**
     * Add a single item to the left menu;
     * <p/>
     * WARNING: It will be removed from v2.0.
     * @param menuItem menuItem that will be added to left Menu
     */
    @Deprecated
    public void addMenuItem(ResideMenuItem menuItem) {
        this.leftMenuItems.add(menuItem);
        layoutLeftMenu.addView(menuItem);
    }

    /**
     * Add a single items;
     * @param menuItem menu item that will be added to menu
     * @param direction menu targeted to, 0 as left menu, 1 as right menu
     */
    public void addMenuItem(ResideMenuItem menuItem, int direction) {
        if (direction == DIRECTION_LEFT) {
            this.leftMenuItems.add(menuItem);
            layoutLeftMenu.addView(menuItem);
        } else {
            this.rightMenuItems.add(menuItem);
            layoutRightMenu.addView(menuItem);
        }
    }

    /**
     * WARNING: It will be removed from v2.0.
     * @param menuItems list of right menu item
     */
    @Deprecated
    public void setMenuItems(List<ResideMenuItem> menuItems) {
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }

    /**
     * Set menu items by a array;
     * @param menuItems list of menu
     * @param direction menu targeted to, 0 as left menu, 1 as right menu
     */
    public void setMenuItems(List<ResideMenuItem> menuItems, int direction) {
        if (direction == DIRECTION_LEFT)
            this.leftMenuItems = menuItems;
        else
            this.rightMenuItems = menuItems;
        rebuildMenu();
    }

    /**
     * Rebuild Menu,
     * if the menu is not null,remove all of it,then use new list of item to rebuild it
     */
    private void rebuildMenu() {
        if (layoutLeftMenu != null) {
            layoutLeftMenu.removeAllViews();
            for (ResideMenuItem leftMenuItem : leftMenuItems)
                layoutLeftMenu.addView(leftMenuItem);
        }

        if (layoutRightMenu != null) {
            layoutRightMenu.removeAllViews();
            for (ResideMenuItem rightMenuItem : rightMenuItems)
                layoutRightMenu.addView(rightMenuItem);
        }
    }

    /**
     * Get the list of left menu
     * WARNING: It will be removed from v2.0.
     * @return list of left menu
     */
    @Deprecated
    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }

    /**
     * Return instances of menu items;
     * @param direction the direction of menu needed,0 as left,1 as right
     * @return list of menu
     */
    public List<ResideMenuItem> getMenuItems(int direction) {
        if (direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
    }

    /**
     * If you need to do something on closing or opening menu,
     * set a listener here.
     * @param menuListener listener that will be called when menu is open or closed
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Show the menu;
     * @param direction the direction of menu needed,0 as left,1 as right
     */
    public void openMenu(int direction) {

        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /**
     * Close the menu;
     */
    public void closeMenu() {
        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    /**
     * Disable operation of menu specified
     * @param direction the direction of menu needed,0 as left,1 as right
     */
    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    /**
     * Check whether operation of menu designated directed
     * @param direction the direction of menu needed,0 as left,1 as right
     * @return true as enable,false as unable
     */
    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    /**
     * set the direction of scale animation
     * @param direction the direction of menu needed,0 as left,1 as right
     */
    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 1.5f;
        } else {
            scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;
        }

        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     * @return the status of menu,true as open,false as closed
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * monitor click event that occur to activity attached to
     * if menu is open,close it
     */
    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    /**
     * Monitor the operation of animation
     */
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(scrollViewMenu);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu(scrollViewLeftMenu);
                hideScrollViewMenu(scrollViewRightMenu);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    /**
     * A helper method to build scale down animation;
     * @param target view that animation targets to
     * @param targetScaleX X scale down value(0-1)
     * @param targetScaleY Y scale down value(0-1)
     * @return animation that method generates
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        if (mUse3D) {
            int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
            scaleDown.playTogether(ObjectAnimator.ofFloat(target, "rotationY", angle));
        }

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * A helper method to build scale up animation;
     * @param target view that animation targets to
     * @param targetScaleX X scale up value, more than 1
     * @param targetScaleY Y X scale up value, more than 1
     * @return animation that method generates
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        if (mUse3D) {
            scaleUp.playTogether(ObjectAnimator.ofFloat(target, "rotationY", 0));
        }

        scaleUp.setDuration(250);
        return scaleUp;
    }

    /**
     * build menu animation
     * @param target view that animation targets to
     * @param alpha the transparency of view,0-1
     * @return animation that method generates
     */
    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * If there were some view you don't want reside menu
     * to intercept their touch event, you could add it to
     * ignored views.
     * @param v view ignored by interceptor
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * Remove a view from ignored views;
     * @param v view rmoved from ignored list
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * Clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * If the motion event was relative to the view
     * which in ignored view list,return true;
     * @param ev motion event that occur to the view
     * @return true as ignored,false as not
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }
    /**
     *set the direction of menu showing
     * @param currentRawX current touch event x coordinate
     */
    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    /**
     *get the scale value by the X coordinate touched
     * @param currentRawX current touch position x coordinate
     * @return the value of scale
     */
    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    /**
     * handle touch event
     * @param ev event motioned
     * @return true as this handles,false as its child handles
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95)
                        showScrollViewMenu(scrollViewMenu);

                    float targetScale = getTargetScale(ev.getRawX());
                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        ViewHelper.setRotationY(viewActivity, angle);

                        ViewHelper.setScaleX(imageViewShadow, targetScale - shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale - shadowAdjustScaleY);
                    } else {
                        ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    }
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * get the height of the screen
     * @return the height of screen,in pixel
     */
    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * get the width of the screen
     * @return the width of screen,in pixel
     */
    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * set the scale of view
     * @param scaleValue the value of scale
     */
    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    /**
     * enable 3D animation
     * @param use3D true as use,false as not
     */
    public void setUse3D(boolean use3D) {
        mUse3D = use3D;
    }

    /**
     * show menu
     * @param scrollViewMenu the view of menu showed
     */
    private void showScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    /**
     * hide menu
     * @param scrollViewMenu the view of menu hided
     */
    private void hideScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }
}
