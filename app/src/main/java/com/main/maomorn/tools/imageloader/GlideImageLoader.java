package com.main.maomorn.tools.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {

        Glide.with(context).load(path).centerCrop().into(imageView);
    }
}
