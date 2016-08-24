package com.project.wei.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写它的触摸监听，但是什么都不做，让它不能滑动
        return true;
    }

//    事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子控件的事件
    }
}
