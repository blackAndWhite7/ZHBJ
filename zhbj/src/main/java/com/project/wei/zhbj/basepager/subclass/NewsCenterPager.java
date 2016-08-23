package com.project.wei.zhbj.basepager.subclass;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.wei.zhbj.basepager.BasePager;
import com.project.wei.zhbj.global.GlobalConstants;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class NewsCenterPager extends BasePager{

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);

        tv_title.setText("新闻中心");//修改页面标题
        ibtn_menu.setVisibility(View.VISIBLE);// 显示菜单按钮
        // 请求服务器,获取数据
        // 开源框架: XUtils
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.i("tag",result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }
}
