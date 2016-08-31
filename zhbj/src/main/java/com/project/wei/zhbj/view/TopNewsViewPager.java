package com.project.wei.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wei on 2016/8/24 0024.
 */
public class TopNewsViewPager extends ViewPager{

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopNewsViewPager(Context context) {
        super(context);
    }



    /*解决头条新闻的viewpager在滑动时，被父控件viewpager拦截，*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有的父控件以及祖宗控件 不要拦截事件,就是它自己处理这个事件
        getParent().requestDisallowInterceptTouchEvent(true);
        /* 1. 上下滑动需要拦截      需要拦截就是给父控件进行处理
           2. 向右滑动并且当前是第一个页面,需要拦截
           3. 向左滑动并且当前是最后一个页面（该分类的最后一个页面）,需要拦截
           */
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int dx = endX - startX;
                int dy = endY - startY;
                if (Math.abs(dx) > Math.abs(dy)) {
                    //左右滑动
                    int currentItem = getCurrentItem();
                    if (dx > 0) {
                        //向右滑
                        if (currentItem == 0) {
                            //第一个页面，需要拦截，父控件进行处理
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //向左滑
      //////////////////////////////////////////////下面这条语句做个标记////////////////////////
                        int count = getAdapter().getCount();//每个分类的页面总数
                        if (currentItem == count - 1) {
                            //这个分类的最后一个页面，需要拦截，父控件进行处理
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
                    //上下滑动，需要拦截，父控件进行处理
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
