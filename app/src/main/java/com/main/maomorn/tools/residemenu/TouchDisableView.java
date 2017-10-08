package com.main.maomorn.tools.residemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MaoMorn on 2017-04-18.
 */

public class TouchDisableView extends ViewGroup {

    private View mContent;

    //	private int mMode;
    private boolean mTouchDisabled = false;

    public TouchDisableView(Context context) {
        this(context, null);
    }

    public TouchDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(View v) {
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    /**
     * Set the size of ViewGroup or its child
     * @param widthMeasureSpec excepted width
     * @param heightMeasureSpec excepted height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContent.measure(contentWidth, contentHeight);
    }

    /**
     * Assign a size and position to the {@link #mContent}and all of its descendants
     * @param changed size or position of current ViewGroup changed or not,
     *                true as changed,false as not
     * @param l Left position, relative to parent
     * @param t Top position, relative to parent
     * @param r Right position, relative to parent
     * @param b Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    /**
     * Check whether execute Touch Event
     * @param ev Motion Event intercepted
     * @return execute or not,true as true,false as not
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mTouchDisabled;
    }

    /**
     * Enable whether to execute Touch Event
     * @param disableTouch enable or unable,true as enable,false as unable
     */
    void setTouchDisable(boolean disableTouch) {
        mTouchDisabled = disableTouch;
    }

    /**
     * Check whether execute Touch Event
     * @return true as execute,false as not
     */
    boolean isTouchDisabled() {
        return mTouchDisabled;
    }
}