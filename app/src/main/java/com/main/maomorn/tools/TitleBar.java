package com.main.maomorn.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.maomorn.schoolmarket.R;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by MaoMorn on 2017/4/11.
 */
public class TitleBar extends RelativeLayout {
    private ImageView leftImg;
    private TextView leftText;
    private TextView title;
    private ImageView rightImg;
    private TextView rightText;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.tools_titlebar, this, true);
        leftImg = (ImageView) view.findViewById(R.id.left_img);
        leftText = (TextView) view.findViewById(R.id.left_text);
        title = (TextView) view.findViewById(R.id.title);
        rightImg = (ImageView) view.findViewById(R.id.right_img);
        rightText = (TextView) view.findViewById(R.id.right_text);

        if(null!=attrs){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
            Drawable drawable = typedArray.getDrawable(R.styleable.TitleBar_left_img);
            if (null != drawable) {
                leftImg.setImageDrawable(drawable);
            }
            drawable = typedArray.getDrawable(R.styleable.TitleBar_right_img);
            if (null != drawable) {
                rightImg.setImageDrawable(drawable);
            }
            CharSequence text = typedArray.getText(R.styleable.TitleBar_title);
            if (null != text) {
                title.setText(text);
            }
            ColorStateList color = typedArray.getColorStateList(R.styleable.TitleBar_title_color);
            if (null != color) {
                title.setTextColor(color);
            }
            float size = (float) typedArray.getDimensionPixelOffset(R.styleable.TitleBar_title_size, 18);
            title.setTextSize(size);

            text = typedArray.getText(R.styleable.TitleBar_left_text);
            if (null != text) {
                leftText.setText(text);
                leftText.setVisibility(VISIBLE);
                leftImg.setVisibility(GONE);
            }
            text = typedArray.getText(R.styleable.TitleBar_right_text);
            if (null != text) {
                rightText.setText(text);
                rightText.setVisibility(VISIBLE);
                rightImg.setVisibility(GONE);
            }
            color = typedArray.getColorStateList(R.styleable.TitleBar_func_color);
            if (null != color) {
                leftText.setTextColor(color);
                rightText.setTextColor(color);
            }
            size = (float) typedArray.getDimensionPixelOffset(R.styleable.TitleBar_func_size, 14);
            leftText.setTextSize(size);
            rightText.setTextSize(size);

            typedArray.recycle();
        }

    }

    public void setLeftOnClickListener(OnClickListener listener) {
        if (0 == leftImg.getVisibility()) {
            leftImg.setOnClickListener(listener);
        } else {
            leftText.setOnClickListener(listener);
        }
    }

    public void setRightOnClickListener(OnClickListener listener) {
        if (0 == rightImg.getVisibility()) {
            rightImg.setOnClickListener(listener);
        } else {
            rightText.setOnClickListener(listener);
        }
    }

    public void setTitleOnClickListener(OnClickListener listener) {
        title.setOnClickListener(listener);
    }


}
