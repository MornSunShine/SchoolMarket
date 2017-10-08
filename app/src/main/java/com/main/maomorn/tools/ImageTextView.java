package com.main.maomorn.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017/3/4.
 */

public class ImageTextView extends LinearLayout {
    private Context mContext;
    private LinearLayout lL_content;//包裹图片和标题文字的线性布局，用于改变布局方向
    private ImageView image;// 图片
    private TextView text;// 标题文字

    /**
     * 初始化构件的基本成员控件，处理好基本的设置
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).
                inflate(R.layout.tools_imagetextview, this, true);
        lL_content = (LinearLayout) view.findViewById(R.id.lL_content);
        image = (ImageView) view.findViewById(R.id.imagetextview_image);
        text = (TextView) view.findViewById(R.id.imagetextview_text);

        /*如果xml中的ImageViewText控件中的属性不为空，则取出其属性，并赋值到相应控件上*/
        if (null != attrs) {
            TypedArray typedArray = context.
                    obtainStyledAttributes(attrs, R.styleable.ImageTextView);
            /*方向 0-水平，1-垂直，默认为1*/
            int orientation = typedArray.
                    getInt(R.styleable.ImageTextView_android_orientation, 1);
            /*布局*/
            if (LinearLayout.VERTICAL != orientation) {
                /*水平*/
                lL_content.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                /*垂直*/
                lL_content.setOrientation(LinearLayout.VERTICAL);
            }
            /*textview内容*/
            CharSequence text = typedArray.
                    getText(R.styleable.ImageTextView_android_text);
            if (null != text) {
                this.text.setText(text);
            }
            /*textview颜色*/
            ColorStateList color = typedArray.
                    getColorStateList(R.styleable.ImageTextView_android_textColor);
            if (null != color) {
                this.text.setTextColor(color);
            }
            /*text的外边距*/
            int margin_top = typedArray.
                    getDimensionPixelOffset(R.styleable.ImageTextView_text_margin_top, 0);
            int margin_bottom = typedArray.
                    getDimensionPixelOffset(R.styleable.ImageTextView_text_margin_bottom, 0);
            int margin_start = typedArray.
                    getDimensionPixelOffset(R.styleable.ImageTextView_text_margin_start, 0);
            int margin_end = typedArray.
                    getDimensionPixelOffset(R.styleable.ImageTextView_text_margin_end, 0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.text.getLayoutParams();
            layoutParams.setMargins(margin_start, margin_top, margin_end, margin_bottom);
            this.text.setLayoutParams(layoutParams);

            /*textview字体大小*/
            this.text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            /*imageview图片资源*/
            Drawable drawable = typedArray.
                    getDrawable(R.styleable.ImageTextView_android_src);
            if (null != drawable) {
                this.image.setImageDrawable(drawable);
            }
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setAdjustViewBounds(true);
            /*imageview背景*/
            Drawable drawable1Bg = typedArray.
                    getDrawable(R.styleable.ImageTextView_android_background);
            if (null != drawable1Bg) {
                this.image.setBackground(drawable1Bg);
            }
            /*保持以后使用属性的一致性*/
            typedArray.recycle();
        }
        /*对ImageTextView触摸事件的监听*/
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    image.setPressed(true);
                    text.setPressed(true);
                } else if (MotionEvent.ACTION_CANCEL != event.getAction()
                        || MotionEvent.ACTION_UP != event.getAction()) {
                    image.setPressed(false);
                    text.setPressed(false);
                }
                return false;
            }
        });
    }

    /**
     * ImageTextView构造方法，用于在代码里创建ImageTextView对象
     *
     * @param context     上下文
     * @param orientation LinearLayout是否为垂直方向布局 true-垂直，false-水平
     */
    public ImageTextView(Context context, boolean orientation) {
        super(context);
        mContext = context;
        /*设置控件的布局方向*/
        if (orientation) {
            /*垂直*/
            setOrientation(LinearLayout.VERTICAL);
        } else {
            /*水平*/
            setOrientation(LinearLayout.HORIZONTAL);
        }
        setGravity(Gravity.CENTER);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        this.addView(relativeLayout);
        RelativeLayout.LayoutParams imageLP = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLP.addRule(RelativeLayout.CENTER_IN_PARENT);

        /*图片*/
        image = new ImageView(context);
        image.setLayoutParams(imageLP);
        image.setId(R.id.imagetextview_image);
        relativeLayout.addView(image);

        /*文字*/
        text = new TextView(context);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        this.addView(text);// 把textView加到ImageTextView里面，即加到图片和右边图片包裹起来的相对布局的下面或者右边
    }

    /**
     * 设置图片是否可见
     *
     * @param visibility 图片的可见状态
     */
    public void setImageVisibility(int visibility) {
        image.setVisibility(visibility);
    }

    /**
     * 设置图片
     *
     * @param resId 图片资源id
     */
    public void setImageRecourse(int resId) {
        image.setImageDrawable(getResources().getDrawable(resId, null));
    }

    /**
     * 设置文本内容
     *
     * @param str 文本内容
     */
    public void setTextContent(String str) {
        text.setText(str);
    }

    /**
     * 设置文本大小
     *
     * @param size 大小
     */
    public void setTextSize(float size) {
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 设置文本颜色
     *
     * @param color 颜色
     */
    public void setTextColor(int color) {
        text.setTextColor(color);
    }
}
