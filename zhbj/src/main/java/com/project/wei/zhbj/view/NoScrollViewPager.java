package com.project.wei.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class NoScrollViewPager extends ViewPager  {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写它的触摸监听，但是什么都不做，让使用这个viewpager的页面不能滑动
        return true;
    }

//    事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子控件的事件，如果返回true，新闻中心页面将无法滑动
    }


}
