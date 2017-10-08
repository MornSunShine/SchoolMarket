package com.main.maomorn.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by MaoMorn on 2017-04-14.
 */

public class DIYScrollView extends ScrollView {
    private OnScrollListener listener;

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public DIYScrollView(Context context) {
        super(context);
    }

    public DIYScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DIYScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnScrollListener{
        void onScroll(int scrollY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null){
            listener.onScroll(t);
        }
    }
}
