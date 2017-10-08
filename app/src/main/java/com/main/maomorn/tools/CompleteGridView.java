package com.main.maomorn.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by MaoMorn on 2017-05-13.
 */

public class CompleteGridView extends GridView {

    public CompleteGridView(Context context) {
        super(context);
    }

    public CompleteGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
