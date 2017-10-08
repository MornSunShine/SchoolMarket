package com.main.maomorn.tools.imageloader;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by MaoMorn on 2017-04-13.
 */

public interface ImageLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}
