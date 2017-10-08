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

public class TestActivity extends AppCompatActivity{
//    @BindView(R.id.text) TextView mTextView;
//    @BindView(R.id.button) Button mButton;
//    String result;
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what==0x123){
//                mtextview.settext(result);
////                mButton.setText("Successful!!!");
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test);
//        ButterKnife.bind(this);
//        initListeners();
//        initViews();
        Intent mIntent=new Intent(TestActivity.this,MainAct.class);
        startActivity(mIntent);
        finish();
    }

//    @OnClick(R.id.button)
//    public void test(){
//        new Thread(){
//            @Override
//            public void run() {
////                initViews();
////                handler.sendEmptyMessage(0x123);
//            }
//        }.start();
//    }
//
//    private void initViews(){
//        String url = "http://10.0.2.2/test.php";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String IMGUR_CLIENT_ID = "...";
//        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
//                        RequestBody.create(null, "Square Logo"))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
//                .addFormDataPart().build();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            result=response.body().string();
//            Log.d("结果",result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void initListeners(){

    }
}
