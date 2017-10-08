package com.main.maomorn.schoolmarket;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.Toast;

import com.main.maomorn.schoolmarket.main.HomeFrag;
import com.main.maomorn.schoolmarket.main.PersonalFrag;
import com.main.maomorn.tools.bottombar.BottomBar;
import com.main.maomorn.tools.bottombar.BottomBarBadge;
import com.main.maomorn.tools.bottombar.OnMenuTabClickListener;

/**
 * Created by MaoMorn on 2017/3/4.
 */

public class MainAct extends FragmentActivity {
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止软键盘将页面顶起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.main);
        setDefaultFragment();

//        searchBar=(SearchBar)findViewById(R.id.titlebar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                FragmentTransaction mFragmentTransaction=getSupportFragmentManager().
                        beginTransaction();
                switch (menuItemId){
                    case R.id.menu_recents:
                        mFragmentTransaction.replace(R.id.lL_content,new HomeFrag());
                        break;
                    case R.id.menu_favorites:
                        mFragmentTransaction.replace(R.id.lL_content,new HomeFrag());
                        break;
                    case R.id.menu_nearby:
                        mFragmentTransaction.replace(R.id.lL_content,new HomeFrag());
                        break;
                    case R.id.menu_friends:
                        mFragmentTransaction.replace(R.id.lL_content,new HomeFrag());
                        break;
                    case R.id.menu_food:
                        mFragmentTransaction.replace(R.id.lL_content,new PersonalFrag());
                        break;
                }
                mFragmentTransaction.commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Toast.makeText(getApplicationContext(), "重复点击！！", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");

        //为第一个tab创建一个背景红色内容为13的右上角提醒
        BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(1, "#FF0000", 13);

        // 显示和隐藏的动画持续的时间
        unreadMessages.setAnimationDuration(200);
        unreadMessages.show();
        unreadMessages.setAutoShowAfterUnSelection(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void setDefaultFragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lL_content,new HomeFrag());
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
}
