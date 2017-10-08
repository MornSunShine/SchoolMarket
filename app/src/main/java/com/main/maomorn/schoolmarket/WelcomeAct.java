package com.main.maomorn.schoolmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.main.maomorn.tools.TitleBar;
import com.main.maomorn.until.AppConstants;
import com.main.maomorn.until.SpUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MaoMorn on 2017/2/26.
 */

public class WelcomeAct extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否时第一次启动应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        //如果是第一次则进入功能引导页面
        if(!isFirstOpen){
            Intent intent=new  Intent(WelcomeAct.this,GuideAct.class);
            startActivity(intent);
            finish();
        }else{
            // 如果不是第一次启动app，则正常显示启动屏
            setContentView(R.layout.activity_welcome);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeAct.this, MainAct.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
}
