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
public class SettingPager extends BasePager{

    public SettingPager(Activity activity) {
        super(activity);
    }

    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);

        tv_title.setText("设置");//修改页面标题
        ibtn_menu.setVisibility(View.INVISIBLE);// 隐藏菜单按钮
    }
}
