package com.project.wei.zhbj.basepager.subclass.basepager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.project.wei.zhbj.basepager.BasePager;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SmartServicePager extends BasePager{

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);

        tv_title.setText("智慧服务");//修改页面标题
        ibtn_menu.setVisibility(View.VISIBLE);// 显示菜单按钮

    }
}
