package com.main.maomorn.application;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by MaoMorn on 2017/3/2.
 */

public class CustomApplication extends Application {
    private static final String TAG="CustomApplication";
    /**
     * 程序创建时执行
     */
    @Override
    public void onCreate() {
        // 程序创建的时候执行
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
