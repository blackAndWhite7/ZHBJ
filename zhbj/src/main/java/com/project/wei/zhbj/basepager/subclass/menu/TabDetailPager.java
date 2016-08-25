package com.project.wei.zhbj.basepager.subclass.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.project.wei.zhbj.view.PullRefreshListView;
import com.project.wei.zhbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

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
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.title_indicator)
    private CirclePageIndicator mIndicator;
    @ViewInject(R.id.lv_news)
    private PullRefreshListView lv_news;

    public  String mUrl;
    private ArrayList<NewsContent.TopNews> topnews;
    private ArrayList<NewsContent.NewsData> listNews;
    private ListNewsAdapter listNewsAdapter;

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
        //给listview添加头布局
        View topView = View.inflate(mActivity, R.layout.item_top_news, null);
        ViewUtils.inject(this,topView);//头布局里有三个控件，是通过注解的方式初始化的，所以要再注入头布局
        lv_news.addHeaderView(topView);
        //////////////////////////////////////////////////////////////////////////////////
        //  在这里，处理PullRefreshListView中的下拉刷新事件，它怎么才能知道要进行刷新呢？
        //  通过设置回调！！！ 就是PullRefreshListView，发送要刷新的消息来，然后在这里处理
        // 5. 前端界面设置回调
        //  因为是在PullRefreshListView中设置的方法，所以它的对象才能调用setOnRefreshListener方法
        lv_news.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 最终在这里 刷新数据
                getDataFromServer();
            }
        });
        /*setOnRefreshListener方法小括号里面就是 OnRefreshListener的一个对象listener，有了这个对象，
        才会在PullRefreshListView中去初始化 mListener ，然后判断mListener不为null，才会在PullRefreshListView中调用mListener.onRefresh()，
        这里的onRefresh方法才会执行！！！*/

        /*举个列子，你让你朋友给你去买手机，你要告诉他：setOnRefreshListener方法，必须new出里面的对象，才相当于告诉了他，否则他还是不知道，
        然后他给你看好一个手机，他要问一下你要不要买:onRefresh方法，这个方法里面可以带参数，就想象成手机的一些配置，
        你最后决定要不要买：getDataFromServer，这个方法就是做具体的处理*/
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
                // 这里的缓存很重要，因为你只有把把json存到本地，
                // 才能根据路径找到BitmapUtils缓存的图片，否则，即使缓存了图片，也不会显示出来
                CacheUtil.setCache(mUrl,result,mActivity);
                processData(result);//解析数据

                //在这里进行刷新后续的处理，成功时，收起下拉刷新控件,并更新时间
                lv_news.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 请求失败
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

                //在这里进行刷新后续的处理，失败时，也要收起下拉刷新控件，不更新时间
                lv_news.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        NewsContent newsContent = gson.fromJson(result, NewsContent.class);

        // 头条新闻填充数据
        topnews = newsContent.data.topnews;
        if (topnews!=null) {
            mViewPager.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(mViewPager);  // setAdapter之后，指示器和页面绑定
            mIndicator.setSnap(true);// 快照方式展示
            //页面滑动事件，滑动时改变头条新闻的title,必须用指示器绑定事件
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    NewsContent.TopNews topNews = topnews.get(position);
                    tv_title.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //给第一个默认选中页面中的头条新闻设置title
            tv_title.setText(topnews.get(0).title);
            // 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
            mIndicator.onPageSelected(0);
        }

        // 列表新闻填充数据
        listNews = newsContent.data.news;
        if (listNews != null) {
            listNewsAdapter = new ListNewsAdapter();
            lv_news.setAdapter(listNewsAdapter);
        }
    }


    class TopNewsAdapter extends PagerAdapter {

        public  BitmapUtils bitmapUtils;

        // 这个构造方法，是为了让在 new TopNewsAdapter（）时可以初始化bitmapUtils，写在别的地方也可以
        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            //设置加载中的默认图片
            bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
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
            String topImageURL = topnews.get(position).topimage;// 图片下载链接
            // 下载图片--将图片设置给imageview--避免内存溢出--缓存
            // 上面四件事情，BitmapUtils就可以完成，真踏马牛比.
            bitmapUtils.display(view,topImageURL);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ListNewsAdapter extends BaseAdapter {

        private final BitmapUtils bitmapUtils;

        public ListNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return listNews.size();
        }

        @Override
        public NewsContent.NewsData getItem(int position) {
            return listNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_list_news, null);
                holder = new ViewHolder();
                holder.imageViewIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.textViewTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.textViewDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                 holder = (ViewHolder) convertView.getTag();
            }
            NewsContent.NewsData data = getItem(position);
            holder.textViewTitle.setText(data.title);
            holder.textViewDate.setText(data.pubdate);

            bitmapUtils.display(holder.imageViewIcon,data.listimage);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView imageViewIcon;
        public TextView textViewTitle;
        public TextView textViewDate;
    }


}
