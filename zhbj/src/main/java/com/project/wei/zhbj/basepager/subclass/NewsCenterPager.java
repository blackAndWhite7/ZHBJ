package com.project.wei.zhbj.basepager.subclass;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.wei.zhbj.activity.MainActivity;
import com.project.wei.zhbj.basepager.BaseMenuDetailPager;
import com.project.wei.zhbj.basepager.BasePager;
import com.project.wei.zhbj.basepager.subclass.menu.InteractMenuDetailPager;
import com.project.wei.zhbj.basepager.subclass.menu.NewsMenuDetailPager;
import com.project.wei.zhbj.basepager.subclass.menu.PhotosMenuDetailPager;
import com.project.wei.zhbj.basepager.subclass.menu.TopicMenuDetailPager;
import com.project.wei.zhbj.domain.NewsMenu;
import com.project.wei.zhbj.fragment.LeftMenuFragment;
import com.project.wei.zhbj.global.GlobalConstants;
import com.project.wei.zhbj.utils.CacheUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class NewsCenterPager extends BasePager{

    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 菜单详情页集合
    private NewsMenu mNewsData;// 分类信息网络数据

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    public void initData(){
      /* 默认是菜单详情页-新闻，所以不需要这个页面了
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextSize(50);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);*/

        tv_title.setText("新闻中心");//修改页面标题
        ibtn_menu.setVisibility(View.VISIBLE);// 显示菜单按钮
        // 先判断有没有缓存,如果有的话,就加载缓存
        String cache = CacheUtil.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        //本应该else中，即没有缓存的时候，调用getDataFromServer()来从服务器获取数据，由于服务器可能会
        // 更新数据，以至于不能得到最新的数据，应该先显示以前的缓存，然后再加载最新的数据，
        // 所以getDataFromServer()必须要执行，
        //   还有一种解决办法，就是每个一定时间清理缓存，然后就可以获取到最新的数据

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
                        //把获取出来的json数据存放起来
                        CacheUtil.setCache(GlobalConstants.CATEGORY_URL,result,mActivity);
                        Log.i("服务器数据：",result);
                        //解析json数据
                        processData(result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        // 请求失败
                        e.printStackTrace();
                        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void processData(String result) {
        //利用Gson框架来解析json数据，一定搞懂原理
        Gson gson = new Gson();
        //把解析出来的数据存放到了NewsMenu类中
        mNewsData = gson.fromJson(result, NewsMenu.class);
        Log.i("解析数据：", mNewsData.toString());
        // 获取侧边栏对象
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsData.data);

        // 初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();

        //初始化NewsMenuDetailPager时，把mNewsData.data.get(0).children在构造函数中传递过去
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        // 将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }

    // 设置菜单详情页
    public void setCurrentDetailPager(int position) {
        // 重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);// 获取当前应该显示的页面
        View view = pager.mRootView;// 当前页面的布局，返回的是填充当前页面的view,即每个页面中initView返回的view对象

         // 清除之前旧的布局，否则会重叠显示
        fl_content.removeAllViews();

        fl_content.addView(view);// 给帧布局添加布局

        // 同时初始化页面数据，即调用当前页面的initData方法
        pager.initData();

        // 更新标题
        tv_title.setText(mNewsData.data.get(position).title);
    }
}
