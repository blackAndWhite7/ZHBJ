package com.project.wei.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.fragment.ContentFragment;
import com.project.wei.zhbj.fragment.LeftMenuFragment;
import com.umeng.analytics.MobclickAgent;

/* 屏幕适配
> 养成良好的开发习惯: 多用dp,sp,不用px; 多用线性布局和相对布局, 不用绝对布局;
  代码中如果必须设置像素的话, 将dp转为px进行设置
> 项目开发后期,对适配问题进行验证
- 图片适配
	hdpi: 480*800  1.5
	xhdpi: 1280*720  2
	xxhdpi: 1920*1080  3
	设备密度:
	常规做法: 做一套图 1280*720 切图, 放在hdpi或xhdpi下. 如果某个屏幕出了问题, 再针对该屏幕, 对相关出问题的图片进行替换.
- 布局适配(不太常用)
	layout-800x480:专门针对480*800屏幕适配的布局文件, 一般只调整位置和大小, 不建议对控件类型和个数进行调整
- 尺寸适配(很常用)
	//dp 和 px
	//values-1280x720/dimens.xml
- 权重适配
	 android:weightSum="3"
- 代码适配
*/
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);  // 设置侧边栏 默认在左边
        SlidingMenu slidingMenu = getSlidingMenu();
       /* // 设置右侧侧边栏
        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        // 设置模式,左右都有侧边栏
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);*/
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
        //slidingMenu.setBehindOffset(350);//屏幕预留350像素宽度
//      屏幕适配
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        slidingMenu.setBehindOffset(widthPixels * 2/3);


        //初始化fragment
        initFragment();
    }

    public void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();//开始事务
        // 用fragment替换侧边栏和主界面的帧布局，就是填充帧布局;
        // 参1:帧布局容器的id;参2:是要替换的fragment;参3:标记，方便以后找到Fragment
        beginTransaction.replace(R.id.fl_leftmenu,new LeftMenuFragment(),TAG_LEFTMENU);
        beginTransaction.replace(R.id.fl_content,new ContentFragment(),TAG_CONTENT);
        beginTransaction.commit();//提交事务
        //根据标记找到对应的fragment
        // Fragment LeftMenuFragment = fragmentManager.findFragmentByTag(TAG_LEFTMENU);
    }
    // 获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 根据标记找到对应的fragment
        LeftMenuFragment leftMenuFragement = (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFTMENU);
        return leftMenuFragement;
    }

    public ContentFragment getContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 根据标记找到对应的fragment
        ContentFragment ContentFragment = (ContentFragment) fragmentManager.findFragmentByTag(TAG_CONTENT);
        return ContentFragment;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
