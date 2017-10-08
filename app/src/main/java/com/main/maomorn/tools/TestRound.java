package com.main.maomorn.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by MaoMorn on 2017-04-18.
 */

public class TestRound extends ImageView {
    private float[] radiusArray = { 500f, 1000f, 1000f, 1000f, 1000f, 1000f, 1000f, 1000f };

    public TestRound(Context context) {
        super(context);
    }

    public TestRound(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置四个角的圆角半径
     */
    public void setRadius(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        radiusArray[0] = leftTop;
        radiusArray[1] = leftTop;
        radiusArray[2] = rightTop;
        radiusArray[3] = rightTop;
        radiusArray[4] = rightBottom;
        radiusArray[5] = rightBottom;
        radiusArray[6] = leftBottom;
        radiusArray[7] = leftBottom;

        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radiusArray, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
