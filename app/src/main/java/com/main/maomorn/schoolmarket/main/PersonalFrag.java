package com.main.maomorn.schoolmarket.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.main.maomorn.schoolmarket.R;
import com.main.maomorn.tools.CircleImageView;
import com.main.maomorn.tools.TitleBar;
import com.main.maomorn.tools.residemenu.ResideMenu;
import com.main.maomorn.tools.residemenu.ResideMenuItem;
import com.main.maomorn.tools.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MaoMorn on 2017-04-15.
 */

public class PersonalFrag extends Fragment {
    private TitleBar titleBar;
    private RoundedImageView profileBg;
    private CircleImageView headPortrait;

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    // 头像目录
    private final String PATH = Environment.getExternalStorageDirectory() + "/SchoolMarket/HeadPortrait/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_personal, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titlebar);
        profileBg = (RoundedImageView) view.findViewById(R.id.profile_bg);
        headPortrait = (CircleImageView) view.findViewById(R.id.head_portrait);
        Bitmap bt = BitmapFactory.decodeFile(PATH);// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            Drawable drawable = new BitmapDrawable(null, bt);// 转换成drawable
            headPortrait.setImageDrawable(drawable);
        } else {
/**
 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
 *
 */
        }

        setUpMenu();
        initListener();


        return view;
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(getContext());
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.background);
        resideMenu.attachToActivity(getActivity());
//        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(getContext(), R.drawable.icon_location, "Home");

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        /*findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });*/
        /*findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });*/

    }

    void initListener() {
        titleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        headPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"头像设置成功！",Toast.LENGTH_SHORT).show();
                showTypeDialog();
            }
        });
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showTypeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        View view = View.inflate(getContext(), R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(PATH+"head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * 要求设置头像图片
     * @param requestCode 请求码
     * @param resultCode 结果返回码
     * @param data 管道传递的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(PATH);
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    if (head != null) {
/**
 * 上传服务器代码
 */
                        setPicToView(head);// 保存在SD卡中
                        headPortrait.setImageBitmap(head);// 在头像展示控件中显示
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     * @param uri 待裁剪图片的路径
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，1：1正方形裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，默认宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(PATH);
        file.mkdirs();// 创建文件夹
        try {
            b = new FileOutputStream(PATH + "head.jpg");
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
