package com.project.wei.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.activity.MainActivity;
import com.project.wei.zhbj.basepager.BasePager;
import com.project.wei.zhbj.basepager.subclass.GovAffairsPager;
import com.project.wei.zhbj.basepager.subclass.HomePager;
import com.project.wei.zhbj.basepager.subclass.NewsCenterPager;
import com.project.wei.zhbj.basepager.subclass.SettingPager;
import com.project.wei.zhbj.basepager.subclass.SmartServicePager;
import com.project.wei.zhbj.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager vp_content;
    private RadioGroup rg_content;

    private ArrayList<BasePager> mPagers;// 五个标签页的集合

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_content = (RadioGroup) view.findViewById(R.id.rg_content);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();
        //添加五个标签页
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        vp_content.setAdapter(new ViewPagerAdapter());
//      设置每个RadioButton选择后的事件处理，跳转到相应的pager
        radioGruopListener();

        pagerChangeListener();
    }

    private void pagerChangeListener() {
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();//在这里初始化数据

                if (position == 0 || position == mPagers.size() - 1) {
                    //首页和设置页面要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    //其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 手动加载第一页数据，因为第一个页面是默认选中的，所以第一个页面不会加载数据
        mPagers.get(0).initData();
        //同时，第一个页面要禁用侧边栏
        setSlidingMenuEnable(false);
    }

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

    private void radioGruopListener() {
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0,false);//参数二：是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        vp_content.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4,false);
                        break;
                }
            }
        });
    }


    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);

            //在这里调用每个viewpager的这个方法，初始化数据
            //viewpager会默认加载下一个页面的数据,为了节省流量和性能,
            // 不要在此处调用初始化数据的方法,而是监听每个viewpager，当它被选择后再初始化数据
//            basePager.initData();

            View view = basePager.mRootView;//获取当前页面对象的布局
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    // 获取新闻中心页面
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }

}
