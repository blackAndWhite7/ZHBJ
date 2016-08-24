package com.project.wei.zhbj.basepager.subclass.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.basepager.BaseMenuDetailPager;
import com.project.wei.zhbj.domain.NewsContent;
import com.project.wei.zhbj.domain.NewsMenu;
import com.project.wei.zhbj.global.GlobalConstants;
import com.project.wei.zhbj.utils.CacheUtil;
import com.project.wei.zhbj.view.TopNewsViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
//  继承BaseMenuDetailPager，并没有什么关联，只是刚好有它需要的方法
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
//    private TextView view;

    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;
    public  String mUrl;
    private ArrayList<NewsContent.TopNews> topnews;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;

        mUrl = GlobalConstants.SERVER_URL + mTabData.url;//每个页面具体内容的url
    }

    @Override
    public View initView() {
/*        view = new TextView(mActivity);
//        view.setText(mTabData.title);  此处空指针
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);*/
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
  /*      view.setText(mTabData.title);
        Log.i("tag",mTabData.title);*/
        //先去查看缓存，有的话先加载缓存，然后再去服务器请求数据
        String cache = CacheUtil.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,mUrl , new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //把下载的json数据存放到缓存
                CacheUtil.setCache(mUrl,result,mActivity);

                processData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 请求失败
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        NewsContent newsContent = gson.fromJson(result, NewsContent.class);
        Log.i("newsContent",newsContent.toString());
        // 头条新闻填充数据
        topnews = newsContent.data.topnews;
        if (topnews!=null) {
            mViewPager.setAdapter(new TopNewsAdapter());
        }


    }


    class TopNewsAdapter extends PagerAdapter {

        public  BitmapUtils bitmapUtils;

        // 这个构造方法，是为了让在 new TopNewsAdapter（）时可以初始化bitmapUtils，写在别的地方也可以
        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }
        @Override

        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片缩放方式，宽高填充父控件
            //            view.setImageResource(R.drawable.topnews_item_default);
            String topImageURL = topnews.get(position).topimage;// 图片下载链接
            // 下载图片--将图片设置给imageview--避免内存溢出--缓存
            // 上面四件事情，BitmapUtils就可以完成，真踏马牛比
            bitmapUtils.display(view,topImageURL);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
