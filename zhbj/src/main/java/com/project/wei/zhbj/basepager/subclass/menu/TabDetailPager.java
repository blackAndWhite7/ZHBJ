package com.project.wei.zhbj.basepager.subclass.menu;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.project.wei.zhbj.basepager.BaseMenuDetailPager;
import com.project.wei.zhbj.domain.NewsMenu;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
//  继承BaseMenuDetailPager，并没有什么关联，只是刚好有它需要的方法
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    private TextView view;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {
        view = new TextView(mActivity);
//        view.setText(mTabData.title);  此处空指针
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void initData() {
        view.setText(mTabData.title);
        Log.i("tag",mTabData.title);
    }
}
