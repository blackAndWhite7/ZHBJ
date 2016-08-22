package com.project.wei.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.fragment.ContentFragment;
import com.project.wei.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFTMENU = "TAG_LEFTMENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    /* 使用slidingmenu
         1. 引入slidingmenu库
         2. 继承SlidingFragmentActivity
         3. onCreate改成public
         4. 调用相关api
        */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);  // 设置侧边栏 默认在左边
        SlidingMenu slidingMenu = getSlidingMenu();
       /* // 设置右侧侧边栏
        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        // 设置模式,左右都有侧边栏
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);*/
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
        slidingMenu.setBehindOffset(800);//屏幕预留800像素宽度

        initFragment();
    }

    public void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.replace(R.id.fl_leftmenu,new LeftMenuFragment(),TAG_LEFTMENU);
        beginTransaction.replace(R.id.fl_content,new ContentFragment(),TAG_CONTENT);
        beginTransaction.commit();
    }
}
