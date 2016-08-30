package com.project.wei.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.project.wei.zhbj.R;
import com.project.wei.zhbj.utils.SharedPrefUtil;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends Activity {

    private ImageView iv_splash_welcome;
    private RelativeLayout rv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_splash_welcome = (ImageView) findViewById(R.id.iv_splash_welcome);
        rv_splash = (RelativeLayout) findViewById(R.id.rv_splash);
        // 旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0);
        rotateAnimation.setDuration(1000);// 动画时间
        rotateAnimation.setFillAfter(true);// 保持动画结束状态
        // 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        // 渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        // 动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        // 启动动画  iv_splash_welcome 这张图片产生动画效果
        iv_splash_welcome.startAnimation(animationSet);
//        rv_splash.startAnimation(animationSet);//整个布局产生动画效果

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束,跳转页面
                // 如果是第一次进入, 跳新手引导
                // 否则跳主页面
                Boolean isFirstEnter = SharedPrefUtil.getBoolean(getApplicationContext(),
                        "is_first_enter", true);
                if (isFirstEnter) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                // 结束当前页面
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
