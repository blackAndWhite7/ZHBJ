package com.project.wei.zhbj.basepager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.wei.zhbj.R;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
// 五个标签页的基类
public class BasePager {
    public Activity mActivity;
    public View mRootView;
    public TextView tv_title;
    public ImageButton ibtn_menu;
    public FrameLayout fl_content; // 空的帧布局对象，要在子类中动态添加布局

    public BasePager(Activity activity){
        mActivity = activity;
        mRootView = initView();// 当前页面的布局对象
    }
//  初始化布局
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ibtn_menu = (ImageButton) view.findViewById(R.id.ibtn_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }
//   初始化数据
    public void initData(){

    }
}
