package com.project.wei.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.wei.zhbj.R;
import com.project.wei.zhbj.utils.SharedPrefUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private int [] mImageIds = new int[]{R.drawable.guide_1,
            R.drawable.guide_2,R.drawable.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_container;
    private ImageView iv_red_point;
    private int distance;
    private Button btn_guide_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题,
        // 必须在setContentView之前调用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        btn_guide_welcome = (Button) findViewById(R.id.btn_guide_welcome);

        initData();//初始化数据
        vp_guide.setAdapter(new GuideAdapter());//设置数据
        //获取小圆点之间的间隔距离
        getDistance();
        //更新红色圆点的位置
        updateRedLocation();

        btn_guide_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击开始后，下次打开这个应用就不需要重新进入引导界面了，把它改为false
                SharedPrefUtil.setBoolean(getApplicationContext(),"is_first_enter",false);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    private void initData() {
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(mImageIds[i]);// 通过设置背景,可以让宽高填充布局
            //            imageView.setImageResource(mImageIds[i]);  这种方式能不能填充布局取决于图片的大小
            mImageViewList.add(imageView);

            //初始化三个灰色小圆点
            ImageView grayPoint = new ImageView(getApplicationContext());
            grayPoint.setImageResource(R.drawable.shape_point_grey);
            //初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数，给三个小圆点设置间距
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 从第二个点开始设置左边距
            if (i > 0) {
                params.leftMargin = 30;
            }
            grayPoint.setLayoutParams(params);// 设置布局参数
            ll_container.addView(grayPoint);// 给容器添加圆点
        }
    }

    private void getDistance() {
        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure -> layout (确定位置) -> draw (activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树  这里也可以用 iv_red_point 小红点来获取这个视图树
        ll_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // layout方法执行结束的回调
            @Override
            public void onGlobalLayout() {
                // 移除监听,避免重复回调  不移除的话，这个方法会调用多次
                ll_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获得两个圆点之间的距离，是圆心之间的距离的长度
                distance = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
                Log.i("tag",distance+"");
            }
        });
    }

    private void updateRedLocation() {
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 当页面滑动过程中的回调
            @Override                              //positionOffset 移动偏移百分比
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //在滚动的时候更新红色小圆点的位置
                float leftMargin = distance * (positionOffset + position);//小红点当前的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        iv_red_point.getLayoutParams();
                layoutParams.leftMargin = (int) leftMargin;// 修改左边距
                iv_red_point.setLayoutParams(layoutParams);// 重新设置布局参数
            }
            // 某个页面被选中，回调这个方法
            @Override
            public void onPageSelected(int position) {
                //滑动到最后一个引导界面后，显示出button按钮
                if (position == mImageViewList.size() - 1) {
                    btn_guide_welcome.setVisibility(View.VISIBLE);
                } else {
                    btn_guide_welcome.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面状态发生变化的回调
            }
        });
    }

    class GuideAdapter extends PagerAdapter{
        // item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }
        // 初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            //一定要添加到容器中,否则不会显示return回去的页面
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        // 销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
