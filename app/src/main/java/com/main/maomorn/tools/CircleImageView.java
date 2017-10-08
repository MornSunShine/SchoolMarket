package com.main.maomorn.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017/3/5.
 */

public class CircleImageView extends ImageView {
    /*居中填充（铺满）*/
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    /*32位ARGB位图*/
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    /**/
    private static final int COLORDRAWABLE_DIMENSION = 1;
    /*边框默认宽为0,默认颜色为黑*/
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;

    private final RectF mDrawableRect = new RectF();
    private final RectF mInBorderRect = new RectF();
    private final RectF mMidBorderRect = new RectF();
    private final RectF mOutBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mInBorderPaint = new Paint();
    private final Paint mMidBorderPaint = new Paint();
    private final Paint mOutBorderPaint = new Paint();

    private int mInBorderColor = DEFAULT_BORDER_COLOR;
    private int mInBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mMidBorderColor = DEFAULT_BORDER_COLOR;
    private int mMidBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mOutBorderColor = DEFAULT_BORDER_COLOR;
    private int mOutBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mInBorderRadius;
    private float mMidBorderRadius;
    private float mOutBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CircleImageView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param attrs    属性集
     * @param defStyle 默认样式
     */
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.CircleImageView, defStyle, 0);

        mInBorderWidth = typedArray.getDimensionPixelSize(
                R.styleable.CircleImageView_in_border_width, DEFAULT_BORDER_WIDTH);
        mInBorderColor = typedArray.getColor(
                R.styleable.CircleImageView_in_border_color, DEFAULT_BORDER_COLOR);
        mMidBorderWidth = typedArray.getDimensionPixelSize(
                R.styleable.CircleImageView_mid_border_width, DEFAULT_BORDER_WIDTH);
        mMidBorderColor = typedArray.getColor(
                R.styleable.CircleImageView_mid_border_color, DEFAULT_BORDER_COLOR);
        mOutBorderWidth = typedArray.getDimensionPixelSize(
                R.styleable.CircleImageView_out_border_width, DEFAULT_BORDER_WIDTH);
        mOutBorderColor = typedArray.getColor(
                R.styleable.CircleImageView_out_border_color, DEFAULT_BORDER_COLOR);

        typedArray.recycle();

        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    /**
     * 返回缩放类型
     *
     * @return 缩放类型的枚举值
     */
    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    /**
     * 设置缩放类型
     *
     * @param scaleType 缩放类型的枚举值
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (SCALE_TYPE != scaleType) {
            throw new IllegalArgumentException(
                    String.format("ScaleType %s not supported.", scaleType));
        }
    }

    /**
     * 绘制控件
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /*有图片资源，绘制圆*/
        if (null != getDrawable()) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mInBorderRadius, mInBorderPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mMidBorderRadius, mMidBorderPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mOutBorderRadius, mOutBorderPaint);
        }
    }

    /**
     * 控件大小改变时调用
     *
     * @param w    宽度
     * @param h    高度
     * @param oldw 原始宽度
     * @param oldh 原始高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    /**
     * 获取内侧边框颜色
     *
     * @return 边框颜色
     */
    public int getInBorderColor() {
        return mInBorderColor;
    }

    /**
     * 设置内侧边框颜色
     *
     * @param borderColor 边框颜色
     */
    public void setInBorderColor(int borderColor) {
        if (borderColor != mInBorderColor) {
            mInBorderColor = borderColor;
            mInBorderPaint.setColor(mInBorderColor);
            invalidate();
        }
    }

    /**
     * 获取内侧边框宽度
     *
     * @return 边框宽度
     */
    public int getInBorderWidth() {
        return mInBorderWidth;
    }

    /**
     * 设置内侧边框宽度
     *
     * @param borderWidth 边框宽度
     */
    public void setInBorderWidth(int borderWidth) {
        if (borderWidth != mInBorderWidth) {
            mInBorderWidth = borderWidth;
            setup();
        }
    }

    /**
     * 获取中间边框颜色
     *
     * @return 边框颜色
     */
    public int getMidBorderColor() {
        return mMidBorderColor;
    }

    /**
     * 设置中间边框颜色
     *
     * @param borderColor 边框颜色
     */
    public void setMidBorderColor(int borderColor) {
        if (borderColor != mMidBorderColor) {
            mMidBorderColor = borderColor;
            mMidBorderPaint.setColor(mMidBorderColor);
            invalidate();
        }
    }

    /**
     * 获取中间边框宽度
     *
     * @return 边框宽度
     */
    public int getMidBorderWidth() {
        return mMidBorderWidth;
    }

    /**
     * 设置中间边框宽度
     *
     * @param borderWidth 边框宽度
     */
    public void setMidBorderWidth(int borderWidth) {
        if (borderWidth != mMidBorderWidth) {
            mMidBorderWidth = borderWidth;
            setup();
        }
    }

    /**
     * 获取外侧边框颜色
     *
     * @return 边框颜色
     */
    public int getOutBorderColor() {
        return mOutBorderColor;
    }

    /**
     * 设置外侧边框颜色
     *
     * @param borderColor 边框颜色
     */
    public void setOutBorderColor(int borderColor) {
        if (borderColor != mOutBorderColor) {
            mOutBorderColor = borderColor;
            mOutBorderPaint.setColor(mOutBorderColor);
            invalidate();
        }
    }

    /**
     * 获取外侧边框宽度
     *
     * @return 边框宽度
     */
    public int getOutBorderWidth() {
        return mOutBorderWidth;
    }

    /**
     * 设置外侧边框宽度
     *
     * @param borderWidth 边框宽度
     */
    public void setOutBorderWidth(int borderWidth) {
        if (borderWidth != mOutBorderWidth) {
            mOutBorderWidth = borderWidth;
            setup();
        }
    }

    /**
     * 设置位图内容
     *
     * @param bm 位图
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    /**
     * 设置图片（节省资源，高效）
     *
     * @param drawable 图片内容
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    /**
     * 设置图片资源
     *
     * @param resId 图片资源id
     */
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    /**
     * Drawable转位图
     *
     * @param drawable Drawable资源
     * @return 位图
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(
                        COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mInBorderPaint.setStyle(Paint.Style.STROKE);
        mInBorderPaint.setAntiAlias(true);
        mInBorderPaint.setColor(mInBorderColor);
        mInBorderPaint.setStrokeWidth(mInBorderWidth);

        mMidBorderPaint.setStyle(Paint.Style.STROKE);
        mMidBorderPaint.setAntiAlias(true);
        mMidBorderPaint.setColor(mMidBorderColor);
        mMidBorderPaint.setStrokeWidth(mMidBorderWidth);

        mOutBorderPaint.setStyle(Paint.Style.STROKE);
        mOutBorderPaint.setAntiAlias(true);
        mOutBorderPaint.setColor(mOutBorderColor);
        mOutBorderPaint.setStrokeWidth(mOutBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mOutBorderRect.set(0, 0, getWidth(), getHeight());
        mOutBorderRadius = Math.min((getHeight() - mOutBorderWidth) / 2, (getWidth() - mOutBorderWidth) / 2);

        mMidBorderRect.set(mOutBorderWidth,
                mOutBorderWidth,
                getWidth() - mOutBorderWidth,
                getHeight() - mOutBorderWidth);
        mMidBorderRadius = Math.min((mMidBorderRect.height() - mMidBorderWidth) / 2, (mMidBorderRect.width() - mMidBorderWidth) / 2);

        mInBorderRect.set(mOutBorderWidth + mMidBorderWidth,
                mOutBorderWidth + mMidBorderWidth,
                getWidth() - mOutBorderWidth - mMidBorderWidth,
                getHeight() - mOutBorderWidth - mMidBorderWidth);
        mInBorderRadius = Math.min((mInBorderRect.height() - mInBorderWidth) / 2, (mInBorderRect.width() - mInBorderWidth) / 2);

        mDrawableRect.set(mOutBorderWidth + mMidBorderWidth + mInBorderWidth,
                mOutBorderWidth + mMidBorderWidth + mInBorderWidth,
                getWidth() - mOutBorderWidth - mMidBorderWidth - mInBorderWidth,
                getHeight() - mOutBorderWidth - mMidBorderWidth - mInBorderWidth);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    /**
     * 更新阴影
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mInBorderWidth, (int) (dy + 0.5f) + mInBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
}
