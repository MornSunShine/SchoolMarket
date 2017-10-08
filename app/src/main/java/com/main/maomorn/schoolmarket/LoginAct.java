package com.main.maomorn.schoolmarket;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MaoMorn on 2017/3/2.
 */

public class LoginAct extends AppCompatActivity {
    private static String TAG="LoginActivity";
    private RelativeLayout parent;
    /*用户名*/
    private EditText username;
    /*密码*/
    private EditText password;
    /*登录*/
    private Button login;
    /*注册*/
    private TextView register;
    /*忘记密码*/
    private TextView forget;
    /*学校徽标*/
    private ImageView logo;
    /*背景图片*/
    private LinearLayout background;
    /*蒙板*/
    private LinearLayout mask;
    private TranslateAnimation logo_translate;
    private ScaleAnimation logo_scale;

    /**
     *Activity的生命周期由此开始
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.log_in);
        initMember();
        initListener();
        initAnimation();
        Log.d(TAG,"onCreate");
    }

    private void initMember(){
        parent=(RelativeLayout)findViewById(R.id.Parent);
        username=(EditText)findViewById(R.id.stu_number);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.register);
        forget=(TextView)findViewById(R.id.forget);
        logo=(ImageView)findViewById(R.id.logo);
        background=(LinearLayout)findViewById(R.id.back_image);
        mask=(LinearLayout)findViewById(R.id.mask);
    }
    private void initListener(){
        parent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString();
                String pw=password.getText().toString();
                if(name.equals("1427403013")){
                    if(pw.equals("123456")){
                        Toast.makeText(LoginAct.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginAct.this,"密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginAct.this,"错误的用户名！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void initAnimation(){
        logo_scale=new ScaleAnimation(1.0f,0.4f,1.0f,0.4f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        logo_scale.setFillAfter(true);
        logo_scale.setDuration(2000);
        logo_translate= new TranslateAnimation(0.0f,0.0f,0.0f,10.0f);
        logo_translate.setDuration(1000);
        logo_translate.setFillAfter(true);
    }
    /**
     *
     */
    @Override
    public void onStart() {
        logo.startAnimation(logo_scale);
        //        logo.startAnimation(logo_translate);
        Log.d(TAG,"onStart");
        super.onStart();
    }

    /**
     *
     */
    @Override
    public void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
    }
}
