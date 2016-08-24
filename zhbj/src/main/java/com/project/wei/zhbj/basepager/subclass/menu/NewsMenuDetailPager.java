package com.project.wei.zhbj.basepager.subclass.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.activity.MainActivity;
import com.project.wei.zhbj.basepager.BaseMenuDetailPager;
import com.project.wei.zhbj.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 *
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsMenu.NewsTabData> mTabData;//页签网络数据
    private ArrayList<TabDetailPager> mPagers;//页签页面集合

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        //		mActivity = activity;  艹 这个东西用父类的就可以了，麻痹，一直报空指针异常
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //初始化页签
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());//填充mViewPager,即添加mPagers.size()个页面

        mIndicator.setViewPager(mViewPager);//将viewpager和指示器绑定在一起.注意:必须在viewpager设置完数据之后再绑定
        // 设置页面滑动监听
        // mViewPager.setOnPageChangeListener();
        // 此处必须给指示器设置页面监听,不能设置给 mViewPager，因为mIndicator更强势
        pagerChangeListener();
    }

    private void pagerChangeListener() {
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    // 开启侧边栏
                    setSlidingMenuEnable(true);
                } else {
                    //禁用侧边栏
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //  设置侧边栏是否能打开
    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        // 指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();//调试了好多遍，忘了这个了，在这里页面数据初始化
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.btn_next)
    public void nextPager(View view) {
        //跳到下一个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }

}
