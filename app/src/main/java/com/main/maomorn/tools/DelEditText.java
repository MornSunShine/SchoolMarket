package com.main.maomorn.tools;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.graphics.drawable.Drawable;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017/2/25.
 */

public class DelEditText extends EditText implements OnFocusChangeListener, TextWatcher {
    /*删除按钮的资源ID*/
    private final int DELETE_DRAWABLE_ID = R.drawable.icon_editcancel;
    /*类标记*/
    private final String TAG = "DelEditText";
    /*删除按钮*/
    private Drawable mDelDrawable;

    /**
     * 当从代码中创建此控件时，调用该简单构造函数
     *
     * @param context 该控件运行的上下文，通过此参数可以访问当前的主题，资源，等等
     */
    public DelEditText(Context context) {
        super(context);
        init(context);
    }

    /**
     * 当从xml文件创建一个视图的时候调用，提供在xml文件中指定的属性，默认使用空样式，
     * 所以给定的属性只有上下文的主题和给定的属性中的那些
     *
     * @param context 该控件运行的上下文，通过此参数可以访问当前的主题，资源，等等
     * @param attrs   装饰视图的xml标签的属性集
     */
    public DelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 为视图设置属性以及样式
     *
     * @param context      该控件运行的上下文，通过此参数可以访问当前的主题，资源，等等
     * @param attrs        装饰视图的xml标签的属性集
     * @param defStyleAttr 包含提供视图对一个样式资源参照的默认值中的属性，即样式属性
     */
    public DelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     *作为上一构造函数的应急构造函数
     *
     * @param context 该控件运行的上下文，通过此参数可以访问当前的主题，资源，等等
     * @param attrs 装饰视图的xml标签的属性集
     * @param defStyleAttr 包含提供视图对一个样式资源参照的默认值中的属性，即样式属性
     * @param defStyleRes 为视图提供样式的标识符，当defStyleAtter为0，或者不能在当前主题中找到时，启用
     */
    public DelEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化函数，包括删除图标的初始化，以及监听器的设置
     *
     * @param context 上下文
     */
    private void init(Context context) {
        // 监听输入
        mDelDrawable = getCompoundDrawables()[2];
        if (null == mDelDrawable) {
            mDelDrawable = getResources().getDrawable(DELETE_DRAWABLE_ID, null);
        }
        mDelDrawable.setBounds(0, 0, mDelDrawable.getMinimumWidth(), mDelDrawable.getMinimumHeight());
        setDelIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 焦点变化时，判断字符串长度，设置删除图标显示或隐藏
     *
     * @param v        触发该事件的事件源
     * @param hasFocus 窗口状态，是否获得焦点
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setDelIconVisible(getText().length() > 0);
        } else {
            setDelIconVisible(false);
        }
    }

    /**
     * 手机屏幕触摸事件重载
     * 因为不能直接给EditText设置点击事件，所以通过按下的位置模拟点击事件，
     * 当按下的位置在EditText的宽度-图标到控件右边的间距-图标的宽度和
     * EditText的宽度-图标到控件右边的间距之间，就算点击了图标
     *
     * @param event 触摸事件
     * @return 其他回调方法是否重复处理此事件
     * true，不重复处理；false，重复处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != getCompoundDrawables()[2]) {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                boolean touchable = (event.getX() >
                        (getWidth() - getPaddingRight() - mDelDrawable.getIntrinsicWidth()))
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 当输入框里面内容发生变化的时候回调
     *
     * @param s      编辑框内的字符串
     * @param start  选中的文本区域的起始点
     * @param before 修改前的文本区域长度
     * @param count  选中的文本区域的长度
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDelIconVisible(s.length() > 0);
    }

    /**
     * 设置清除图标的显示和隐藏，调用setCompoundDrawables为EditText绘制
     *
     * @param visible 删除图标是否可见
     */
    protected void setDelIconVisible(boolean visible) {
        Drawable right = visible ? mDelDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
        Log.d(TAG, "set show delete ");
    }

    /**
     * 设置晃动动画，默认为5次每秒
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * 晃动动画
     *
     * @param counts 一秒晃动的次数
     * @return 动画对象
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
