package com.main.maomorn.tools;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * 基础更新视图，抽象类
 * Created by MaoMorn on 2017/3/5.
 */

public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {

    private PullToRefreshView mRefreshLayout;//更新视图对象
    private boolean mEndOfRefreshing;//更新是否结束

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param layout  PullToRefreshView对象
     */
    public BaseRefreshView(Context context, PullToRefreshView layout) {
        mRefreshLayout = layout;
    }

    /**
     * 获取PullToRefreshView对象上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mRefreshLayout != null ? mRefreshLayout.getContext() : null;
    }

    /**
     * 获取PullToRefreshView对象
     *
     * @return PullToRefreshView对象
     */
    public PullToRefreshView getRefreshLayout() {
        return mRefreshLayout;
    }

    /**
     * @param percent
     * @param invalidate
     */
    public abstract void setPercent(float percent, boolean invalidate);

    /**
     * 设置偏移距离
     *
     * @param offset 偏移距离
     */
    public abstract void offsetTopAndBottom(int offset);

    /**
     * 重构图片
     *
     * @param who 要求被更新的图片
     */
    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    /**
     * 安排下一帧执行的动画
     *
     * @param who  安排的图像对象
     * @param what 执行的动画
     * @param when 执行的时间延迟，ms
     */
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    /**
     * 图像可以取消预先设定执行的动画
     *
     * @param who  目标图像
     * @param what 取消执行的动画
     */
    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    /**
     * 获取透明度
     *
     * @return 透明度的值
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度的值
     */
    @Override
    public void setAlpha(int alpha) {

    }

    /**
     * 颜色过滤器设置
     *
     * @param cf 色彩过滤器
     */
    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    /**
     * 决定更新结束后的动作
     *
     * @param endOfRefreshing 更新状态(是否结束)
     */
    public void setEndOfRefreshing(boolean endOfRefreshing) {
        mEndOfRefreshing = endOfRefreshing;
    }
}
