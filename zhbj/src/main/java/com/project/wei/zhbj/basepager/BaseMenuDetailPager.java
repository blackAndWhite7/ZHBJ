package com.project.wei.zhbj.basepager;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }
    // 初始化布局,必须子类实现
    public abstract View initView();

    // 初始化数据
    public void initData() {

    }
}
