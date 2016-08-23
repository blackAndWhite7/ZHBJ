package com.project.wei.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.activity.MainActivity;
import com.project.wei.zhbj.basepager.subclass.NewsCenterPager;
import com.project.wei.zhbj.domain.NewsMenu;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class LeftMenuFragment extends BaseFragment {
    //通过XUtils框架中的ViewUtils模块，注解来获取xml文件中的对象
    //注解方式就可以进行UI，资源和事件绑定；无需findViewById和setClickListener等。
    @ViewInject(R.id.lv_list)
    private ListView lv_list;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;// 侧边栏网络数据对象
    private int mCurrentPos;// 当前被选中的item的位置
    private LeftMenuAdapter adapter;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
//        ListView lv_list = (ListView) view.findViewById(R.id.lv_list);
        ViewUtils.inject(this,view);//注入view，才算完成

        return view;
    }
    @Override
    public void initData() {
    }

    // 给侧边栏设置数据
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        mCurrentPos = 0;//当前选中的位置归零
        // 更新页面
        mNewsMenuData = data;
        adapter = new LeftMenuAdapter();
        lv_list.setAdapter(adapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;// 更新当前被选中的位置
                adapter.notifyDataSetChanged();//刷新listview
                // 收起侧边栏
                toggle();
                // 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容
                setCurrentDetailPager(position);
            }
        });
    }
//  设置当前菜单详情页
    protected void setCurrentDetailPager(int position) {
        // 获取新闻中心的对象
        MainActivity mainActivity = (MainActivity) mActivity;
        // 获取ContentFragment
        ContentFragment fragment = mainActivity.getContentFragment();
        // 获取NewsCenterPager
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        // 修改新闻中心的FrameLayout的布局
        newsCenterPager.setCurrentDetailPager(position);
    }

    private void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate( mActivity, R.layout.item_leftmenu, null);
            TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);

            NewsMenu.NewsMenuData newsMenuData = mNewsMenuData.get(position);
            tv_menu.setText(newsMenuData.title);
            if (mCurrentPos == position) {
                // <item android:state_enabled="true" android:color="#F00"/>
                //在文字的状态选择器中 enabled表示的是 按下，即可用的时候
                tv_menu.setEnabled(true);// 被选中,文字变为红色

            } else {
                tv_menu.setEnabled(false);// 未选中,文字变为白色
            }

            return view;
        }
    }
}
