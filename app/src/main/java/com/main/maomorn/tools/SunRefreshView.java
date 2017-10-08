package com.main.maomorn.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.main.maomorn.schoolmarket.R;
import com.main.maomorn.until.Utils;

/**
 * 带有太阳旋转的刷新动画
 * 根据视图占有的宽度，计算动画各组件的尺寸
 * Created by MaoMorn on 2017/3/5.
 */

public class SunRefreshView extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;//动画持续时间

    private final static float SKY_RATIO = 0.65f;//天空比例
    private static final float SKY_INITIAL_SCALE = 1.05f;//天空初始比例

    private final static float TOWN_RATIO = 0.22f;//房屋比例
    private static final float TOWN_INITIAL_SCALE = 1.20f;//房屋初始比例
    private static final float TOWN_FINAL_SCALE = 1.30f;//房屋最终比例

    private static final float SUN_FINAL_SCALE = 0.75f;
    private static final float SUN_INITIAL_ROTATE_GROWTH = 1.2f;//太阳旋转增长初始比例
    private static final float SUN_FINAL_ROTATE_GROWTH = 1.5f;//太阳旋转增长最终比例

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private PullToRefreshView mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;//刷新动画顶端位置
    private int mScreenWidth;//屏幕的宽度

    private int mSkyHeight;//天空的高度
    private float mSkyTopOffset;//天空顶部偏移
    private float mSkyMoveOffset;//天空移动偏移

    private int mTownHeight;//房屋的高度
    private float mTownInitialTopOffset;//房屋初始的顶部偏移
    private float mTownFinalTopOffset;//房屋最终顶部偏移
    private float mTownMoveOffset;//房屋移动偏移

    private int mSunSize = 100;//太阳的尺寸
    private float mSunLeftOffset;//太阳左边偏移
    private float mSunTopOffset;//太阳右边偏移

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private Bitmap mSky;//天空
    private Bitmap mSun;//太阳
    private Bitmap mTown;//房屋

    private boolean isRefreshing = false;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param parent  下拉刷新视图对象
     */
    public SunRefreshView(Context context, final PullToRefreshView parent) {
        super(context, parent);
        mParent = parent;
        mMatrix = new Matrix();

        setupAnimations();
        parent.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(parent.getWidth());
            }
        });
    }

    /**
     * 初始化图形尺寸
     *
     * @param viewWidth 视图尺寸
     */
    public void initiateDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        mScreenWidth = viewWidth;
        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        mSkyTopOffset = (mSkyHeight * 0.38f);
        mSkyMoveOffset = Utils.dpToPixel(getContext(), 15);

        mTownHeight = (int) (TOWN_RATIO * mScreenWidth);
        mTownInitialTopOffset = (mParent.getTotalDragDistance() - mTownHeight * TOWN_INITIAL_SCALE);
        mTownFinalTopOffset = (mParent.getTotalDragDistance() - mTownHeight * TOWN_FINAL_SCALE);
        mTownMoveOffset = Utils.dpToPixel(getContext(), 10);

        mSunLeftOffset = 0.3f * (float) mScreenWidth;
        mSunTopOffset = (mParent.getTotalDragDistance() * 0.1f);

        mTop = -mParent.getTotalDragDistance();

        createBitmaps();
    }

    /**
     * 实例化图片资源
     */
    private void createBitmaps() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        mSky = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sky, options);
        mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);
        mTown = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.buildings, options);
        mTown = Bitmap.createScaledBitmap(mTown, mScreenWidth, (int) (mScreenWidth * TOWN_RATIO), true);
        mSun = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sun, options);
        mSun = Bitmap.createScaledBitmap(mSun, mSunSize, mSunSize, true);
    }

    /**
     * 设置
     *
     * @param percent
     * @param invalidate
     */
    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    /**
     * 顶部到底部的偏移距离
     *
     * @param offset 偏移距离
     */
    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    /**
     * 绘制方法重载
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawSky(canvas);
        drawSun(canvas);
        drawTown(canvas);

        canvas.restoreToCount(saveCount);
    }

    /**
     * 绘制天空
     *
     * @param canvas 画板
     */
    private void drawSky(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /**
             * 在{@link #SKY_INITIAL_SCALE}和1.0f之间，根据{@link #mPercent}改变skyScale
             */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
            skyScale = SKY_INITIAL_SCALE;
        }

        float offsetX = -(mScreenWidth * skyScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() - mSkyTopOffset // Offset canvas moving
                - mSkyHeight * (skyScale - 1.0f) / 2 // Offset sky scaling
                + mSkyMoveOffset * dragPercent; // Give it a little move top -> bottom

        matrix.postScale(skyScale, skyScale);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mSky, matrix, null);
    }

    /**
     * 绘制房屋
     *
     * @param canvas 在该画板内绘制
     */
    private void drawTown(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float townScale;
        float townTopOffset;
        float townMoveOffset;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /**
             * 根据{@link #mPercent},在{@link #TOWN_INITIAL_SCALE}和@link #TOWN_FINAL_SCALE}之间,
             * 改变房屋的比例
             * 根据{@link #mPercent},在{@link #mTownInitialTopOffset}和{@link #mTownFinalTopOffset}之间,
             * 改变房屋顶部偏移
             */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            townScale = TOWN_INITIAL_SCALE + (TOWN_FINAL_SCALE - TOWN_INITIAL_SCALE) * scalePercent;
            townTopOffset = mTownInitialTopOffset - (mTownFinalTopOffset - mTownInitialTopOffset) * scalePercent;
            townMoveOffset = mTownMoveOffset * (1.0f - scalePercent);
        } else {
            float scalePercent = dragPercent / SCALE_START_PERCENT;
            townScale = TOWN_INITIAL_SCALE;
            townTopOffset = mTownInitialTopOffset;
            townMoveOffset = mTownMoveOffset * scalePercent;
        }

        float offsetX = -(mScreenWidth * townScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() // Offset canvas moving
                + townTopOffset
                - mTownHeight * (townScale - 1.0f) / 2 // Offset town scaling
                + townMoveOffset; // Give it a little move

        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mTown, matrix, null);
    }

    /**
     * @param canvas
     */
    private void drawSun(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = mPercent;
        if (dragPercent > 1.0f) { // Slow down if pulling over set height
            dragPercent = (dragPercent + 9.0f) / 10;
        }

        float sunRadius = (float) mSunSize / 2.0f;
        float sunRotateGrowth = SUN_INITIAL_ROTATE_GROWTH;

        float offsetX = mSunLeftOffset;
        float offsetY = mSunTopOffset
                + (mParent.getTotalDragDistance() / 2) * (1.0f - dragPercent) // Move the sun up
                - mTop; // Depending on Canvas position

        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            float sunScale = 1.0f - (1.0f - SUN_FINAL_SCALE) * scalePercent;
            sunRotateGrowth += (SUN_FINAL_ROTATE_GROWTH - SUN_INITIAL_ROTATE_GROWTH) * scalePercent;

            matrix.preTranslate(offsetX + (sunRadius - sunRadius * sunScale), offsetY * (2.0f - sunScale));
            matrix.preScale(sunScale, sunScale);

            offsetX += sunRadius;
            offsetY = offsetY * (2.0f - sunScale) + sunRadius * sunScale;
        } else {
            matrix.postTranslate(offsetX, offsetY);

            offsetX += sunRadius;
            offsetY += sunRadius;
        }

        matrix.postRotate(
                (isRefreshing ? -360 : 360) * mRotate * (isRefreshing ? 1 : sunRotateGrowth),
                offsetX,
                offsetY);

        canvas.drawBitmap(mSun, matrix, null);
    }

    /**
     * @param percent
     */
    public void setPercent(float percent) {
        mPercent = percent;
    }

    /**
     * @param rotate
     */
    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidateSelf();
    }

    /**
     *
     */
    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    /**
     * @param bounds
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSkyHeight + top);
    }

    /**
     * @return
     */
    @Override
    public boolean isRunning() {
        return false;
    }

    /**
     *
     */
    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    /**
     *
     */
    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    /**
     *
     */
    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

}
