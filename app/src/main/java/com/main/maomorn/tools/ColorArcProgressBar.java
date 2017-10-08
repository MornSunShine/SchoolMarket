package com.main.maomorn.tools;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017-04-22.
 */

public class ColorArcProgressBar extends View {

    public static final String TAG = "ColorArcProgressBar";
    public final int DEFAULT_BGARC_WIDTH = dpToPixel(15);
    public final int DEFAULT_PROGRESS_WIDTH = dpToPixel(10);
    public final int DEFAULT_SHORT_DEGREE = dpToPixel(5);
    public final int DEFAULT_LONG_DEGREE = dpToPixel(13);
    public final int DEFAULT_CONTENT_SIZE = spToPixel(68);
    public final int DEFAULT_UNIT_SIZE = spToPixel(25);
    public final int DEFAULT_TITLE_SIZE = spToPixel(20);
    public final float DEFAULT_CONTENT_OFFSET = 0;
    public final float DEFAULT_UNIT_OFFSET = -1;
    public final float DEFAULT_TITLE_OFFSET = 1;
    private final int DEFAULT_DEGREE_PROGRESS_DISTANCE = dpToPixel(10);

    public final int DEFAULT_CURRENT_VALUE = 0;
    public final int DEFAULT_MAX_VALUE = 100;
    public final int DEFAULT_TOTAL_ANGLE = 360;
    public final int DEFAULT_START_ANGLE = -90;

    public final int DEFAULT_UNIT_COLOR = Color.parseColor("#676767");
    public final int DEFAULT_TITLE_COLOR = Color.parseColor("#676767");
    public final int DEFAULT_CONTENT_COLOR = Color.parseColor("#000000");
    public final int DEFAULT_BGARC_COLOR = Color.parseColor("#111111");
    public final int DEFAULT_LONG_DEGREE_COLOR = Color.parseColor("#111111");
    public final int DEFAULT_SHORT_DEGREE_COLOR = Color.parseColor("#111111");

    private float diameter;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint unitPaint;
    private Paint degreePaint;
    private Paint titlePaint;

    private RectF bgRect;

    private PaintFlagsDrawFilter mDrawFilter;
    private Matrix rotateMatrix;

    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float sweepValue;
    private float targetValues = 0;
    private float startAngle = DEFAULT_START_ANGLE;
    private float totalAngle = DEFAULT_TOTAL_ANGLE;
    private float maxValues = DEFAULT_MAX_VALUE;
    private float curValues = DEFAULT_CURRENT_VALUE;
    private float bgArcWidth = DEFAULT_BGARC_WIDTH;
    private float progressWidth = DEFAULT_PROGRESS_WIDTH;
    private float textSize = DEFAULT_CONTENT_SIZE;
    private float unitSize = DEFAULT_UNIT_SIZE;
    private float titleSize = DEFAULT_TITLE_SIZE;
    private float longDegree = DEFAULT_LONG_DEGREE;
    private float shortDegree = DEFAULT_SHORT_DEGREE;

    private float distance = DEFAULT_DEGREE_PROGRESS_DISTANCE;
    private float contet_offset = DEFAULT_CONTENT_OFFSET;
    private float unit_offset = DEFAULT_UNIT_OFFSET;
    private float title_offset = DEFAULT_TITLE_OFFSET;

    private int unitColor = DEFAULT_UNIT_COLOR;
    private int titleColor = DEFAULT_TITLE_COLOR;
    private int contentColor = DEFAULT_CONTENT_COLOR;
    private int longDegreeColor = DEFAULT_LONG_DEGREE_COLOR;
    private int shortDegreeColor = DEFAULT_SHORT_DEGREE_COLOR;
    private int bgArcColor = DEFAULT_BGARC_COLOR;
    private String titleString;
    private String unitString;

    private boolean isNeedTitle = false;
    private boolean isNeedUnit = false;
    private boolean isNeedDial = false;
    private boolean isNeedContent = false;

    private int paddingStart = 0;
    private int paddingTop = 0;
    private int paddingEnd = 0;
    private int paddingBottom = 0;

    //K = totalAngle / maxValues
    private float K;

    public ColorArcProgressBar(Context context) {
        this(context, null, 0);
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    private void initCofig(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
            int color1 = typedArray.getColor(R.styleable.ColorArcProgressBar_progress_color1, Color.GREEN);
            int color2 = typedArray.getColor(R.styleable.ColorArcProgressBar_progress_color2, color1);
            int color3 = typedArray.getColor(R.styleable.ColorArcProgressBar_progress_color3, color1);
            colors = new int[]{color1, color2, color3, color3};
            bgArcColor = typedArray.getColor(R.styleable.ColorArcProgressBar_back_color, DEFAULT_BGARC_COLOR);
            shortDegreeColor = typedArray.getColor(R.styleable.ColorArcProgressBar_short_degree_color, DEFAULT_SHORT_DEGREE_COLOR);
            longDegreeColor = typedArray.getColor(R.styleable.ColorArcProgressBar_long_degree_color, DEFAULT_LONG_DEGREE_COLOR);
            unitColor = typedArray.getColor(R.styleable.ColorArcProgressBar_unit_color, DEFAULT_UNIT_COLOR);
            titleColor = typedArray.getColor(R.styleable.ColorArcProgressBar_pro_title_color, DEFAULT_TITLE_COLOR);
            contentColor = typedArray.getColor(R.styleable.ColorArcProgressBar_content_color, DEFAULT_CONTENT_COLOR);

            startAngle = typedArray.getInteger(R.styleable.ColorArcProgressBar_start_angle, DEFAULT_START_ANGLE);
            totalAngle = typedArray.getInteger(R.styleable.ColorArcProgressBar_total_angle, DEFAULT_TOTAL_ANGLE);

            bgArcWidth = typedArray.getDimension(R.styleable.ColorArcProgressBar_back_width, DEFAULT_BGARC_WIDTH);
            progressWidth = typedArray.getDimension(R.styleable.ColorArcProgressBar_progress_width, DEFAULT_PROGRESS_WIDTH);
            distance = typedArray.getDimension(R.styleable.ColorArcProgressBar_distance, DEFAULT_DEGREE_PROGRESS_DISTANCE);
            contet_offset = typedArray.getFloat(R.styleable.ColorArcProgressBar_content_offset, DEFAULT_CONTENT_OFFSET);
            unit_offset = typedArray.getFloat(R.styleable.ColorArcProgressBar_unit_offset, DEFAULT_UNIT_OFFSET);
            title_offset = typedArray.getFloat(R.styleable.ColorArcProgressBar_title_offset, DEFAULT_TITLE_OFFSET);

            isNeedTitle = typedArray.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
            isNeedContent = typedArray.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
            isNeedUnit = typedArray.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
            isNeedDial = typedArray.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);

            unitString = typedArray.getString(R.styleable.ColorArcProgressBar_string_unit);
            titleString = typedArray.getString(R.styleable.ColorArcProgressBar_string_title);

            textSize = typedArray.getDimension(R.styleable.ColorArcProgressBar_content_size, DEFAULT_CONTENT_SIZE);
            unitSize = typedArray.getDimension(R.styleable.ColorArcProgressBar_unit_size, DEFAULT_UNIT_SIZE);
            titleSize = typedArray.getDimension(R.styleable.ColorArcProgressBar_pro_title_size, DEFAULT_TITLE_SIZE);
            longDegree = typedArray.getDimension(R.styleable.ColorArcProgressBar_long_degree_size, DEFAULT_LONG_DEGREE);
            shortDegree = typedArray.getDimension(R.styleable.ColorArcProgressBar_short_degree_size, DEFAULT_SHORT_DEGREE);

            curValues = typedArray.getFloat(R.styleable.ColorArcProgressBar_current_value, DEFAULT_CURRENT_VALUE);
            maxValues = typedArray.getFloat(R.styleable.ColorArcProgressBar_max_value, DEFAULT_MAX_VALUE);
            typedArray.recycle();
        }
    }

    private void initView() {
        K = totalAngle / maxValues;
        sweepValue = curValues;
        paddingStart = getPaddingStart();
        paddingTop = getPaddingTop();
        paddingEnd = getPaddingEnd();
        paddingBottom = getPaddingBottom();

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(longDegreeColor);

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(bgArcColor);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(colors[0]);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(contentColor);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        unitPaint = new Paint();
        unitPaint.setTextSize(unitSize);
        unitPaint.setColor(unitColor);
        unitPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        titlePaint = new Paint();
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(unitColor);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        rotateMatrix = new Matrix();
        bgRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = measureWidth(widthMeasureSpec);
        int mHeight = measureHeight(heightMeasureSpec);
        int strokeWidth = (int) Math.max(progressWidth, bgArcWidth);
        int diameterX, diameterY;
        if (isNeedDial) {
            //直径
            diameterX = mWidth - paddingStart - paddingEnd - strokeWidth - 2 * (int) (distance + longDegree);
            diameterY = mHeight - paddingTop - paddingBottom - strokeWidth - 2 * (int) (distance + longDegree);
            //圆心
            centerX = paddingStart + distance + longDegree + strokeWidth / 2 + diameterX / 2;
            centerY = paddingTop + distance + longDegree + strokeWidth / 2 + diameterY / 2;
        } else {
            diameterX = mWidth - paddingStart - paddingEnd - strokeWidth;
            diameterY = mHeight - paddingTop - paddingBottom - strokeWidth;
            centerX = paddingStart + strokeWidth / 2 + diameterX / 2;
            centerY = paddingTop + strokeWidth / 2 + diameterY / 2;
        }
        diameter = Math.min(diameterX, diameterY);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 计算控件宽度
     *
     * @param widthMeasureSpec 测量宽度
     * @return 确定宽度，in pixel
     */
    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = 200;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算控件高度
     *
     * @param heightMeasureSpec 测量高度
     * @return 确定高度, in pixel
     */
    private int measureHeight(int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        int result = 200;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        //弧形的矩阵区域
        bgRect.left = centerX - diameter / 2;
        bgRect.top = centerY - diameter / 2;
        bgRect.right = bgRect.left + diameter;
        bgRect.bottom = bgRect.top + diameter;

        //刻度线绘制
        if (isNeedDial) {
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dpToPixel(2));
                    degreePaint.setColor(longDegreeColor);
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - distance,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - distance - longDegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dpToPixel(1.4f));
                    degreePaint.setColor(shortDegreeColor);
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - distance - (longDegree - shortDegree) / 2,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - distance - (longDegree - shortDegree) / 2 - shortDegree, degreePaint);
                }
                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, totalAngle, false, allArcPaint);

        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle, sweepValue * K, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", sweepValue), centerX, centerY - contet_offset * textSize, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(unitString, centerX, centerY - unit_offset * textSize, unitPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - title_offset * textSize, titlePaint);
        }
        invalidate();

    }

    /**
     * 设置目标值,并启动动画
     *
     * @param tarValues 当前进度条显示的刻度值
     * @param duration  动画持续的时间
     */
    public void setTargetValues(float tarValues, int duration) {
        if (tarValues > maxValues) {
            tarValues = maxValues;
        }
        if (tarValues < 0) {
            tarValues = 0;
        }
        targetValues = tarValues;
        setAnimation(curValues * K, targetValues * K, duration);
    }

    /**
     * 为进度设置动画
     *
     * @param last     上一次位置，角度
     * @param current  当前位置，角度
     * @param duration 动画持续时间，ms
     */
    private void setAnimation(float last, float current, int duration) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(duration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepValue = (float) animation.getAnimatedValue() / K;
            }
        });
        progressAnimator.start();
        invalidate();
    }


    /**
     * 设置最大值
     *
     * @param maxValues 进度条显示的最大值
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        K = totalAngle / maxValues;
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth 圆弧的宽度
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth 进度条的宽度
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize 文字的大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param unitSize 单位文字的大小
     */
    public void setunitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    /**
     * 设置单位文字
     *
     * @param unitString 单位文字内容
     */
    public void setUnit(String unitString) {
        this.unitString = unitString;
        invalidate();
    }

    /**
     * 设置标题
     *
     * @param title 进度条中心的内容
     */
    private void setTitle(String title) {
        this.titleString = title;
    }

    /**
     * 设置是否显示标题
     *
     * @param isNeedTitle true as show，false as not
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * 设置是否显示单位文字
     *
     * @param isNeedUnit true as show，false as not
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * 设置是否显示外部刻度盘
     *
     * @param isNeedDial true as show，false as not
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     * Converts dps to pixels nicely.
     *
     * @param dp dimension in dps
     * @return dimension in pixels
     */
    private int dpToPixel(float dp) {
        if (dp < 0) {
            return 0;
        }
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5);
    }

    /**
     * Converts sps to pixels nicely
     *
     * @param sp dimension in sps
     * @return dimension in pixels
     */
    private int spToPixel(float sp) {
        if (sp < 0) {
            return 0;
        }
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (sp * metrics.scaledDensity + 0.5);
    }
}
