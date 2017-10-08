package com.main.maomorn.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017-04-11.
 */

public class SearchBar extends LinearLayout {
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private ImageView mCancel;
    private AutoCompleteTextView mInput;

    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs,defStyle);
        initListener();
    }


    private void initView(AttributeSet attrs, int defStyle) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tools_searchbar, this, true);
        mLeftImageView = (ImageView) view.findViewById(R.id.left_image);
        mRightImageView = (ImageView) view.findViewById(R.id.right_image);
        mInput = (AutoCompleteTextView) view.findViewById(R.id.input_keyword);
        mCancel = (ImageView) view.findViewById(R.id.img_cancel);
        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs, R.styleable.SearchBar, defStyle, 0);
            Drawable img;
            img = typedArray.getDrawable(R.styleable.SearchBar_st_left_img);
            if (null != img) {
                mLeftImageView.setImageDrawable(img);
            }
            img = typedArray.getDrawable(R.styleable.SearchBar_st_right_img);
            if (null != img) {
                mRightImageView.setImageDrawable(img);
            }
            typedArray.recycle();
        }
        mInput.setFocusable(false);
    }

    private void initListener() {
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCancel.setVisibility(VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mRightImageView.performClick();
                }
                return false;
            }
        });
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInput.setText("");
                mCancel.setVisibility(GONE);
            }
        });
    }

    public void setLeftImage(int resImg) {
        mLeftImageView.setImageDrawable(getResources().getDrawable(resImg, null));
    }

    public void setmRightImage(int resImg) {
        mRightImageView.setImageDrawable(getResources().getDrawable(resImg, null));
    }

    public void setLeftClickListener(OnClickListener listener) {
        mLeftImageView.setOnClickListener(listener);
    }

    public void setRightClickListener(OnClickListener listener) {
        mRightImageView.setOnClickListener(listener);
    }

    public void setFocusable(boolean enable) {
        mInput.setFocusable(enable);
    }
}
