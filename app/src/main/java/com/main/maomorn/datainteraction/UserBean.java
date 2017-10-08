package com.main.maomorn.datainteraction;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MaoMorn on 2017-05-15.
 */

public class UserBean {
    private int id;
    private String username;
    private String password;

    public UserBean(String name, String pw) {
        this.username = name;
        this.password = pw;
    }

    public boolean login(){
        OkHttpClient mOkHttpClient=new OkHttpClient();
        String url="http://121.42.177.190/schoolmarket/user/login.php";

//        RequestBody formBody =new FormBody().Builder()
//                .add("name",username)
//                .add("password",password);
//        final Request request=new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//        Call call=mOkHttpClient.newCall(request);
//        try{
//            Response response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return true;
    }

}
