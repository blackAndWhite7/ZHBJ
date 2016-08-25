package com.project.wei.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.wei.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wei on 2016/8/25 0025.
 */
public class PullRefreshListView extends ListView {
    public static final int PULL_TO_REFRESH = 1;
    public static final int RELEASE_TO_REFRESH = 2;
    public static final int REFRESHING = 3;
    public int mCurrentState = PULL_TO_REFRESH;//设置当前默认为刷新状态

    private int viewHeight;
    private View view;
    private  int startY = -1;
    private TextView tv_state;
    private TextView tv_time;
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private RotateAnimation animUP;
    private RotateAnimation animDOWN;

    public PullRefreshListView(Context context) {
        super(context);
        addHeader();
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addHeader();
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addHeader();
    }
//  这个方法，一进来就执行，它放在了三个构造函数中
    public void addHeader() {
        view = View.inflate(getContext(), R.layout.header_refresh_news, null);
        tv_state = (TextView) view.findViewById(R.id.tv_state);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        addHeaderView(view);//给重写的listview添加头布局，后来在TabDetailPager添加的另一个头布局topView，在它下面

        //隐藏头布局
        view.measure(0,0);
        viewHeight = view.getMeasuredHeight();
        view.setPadding(0,-viewHeight,0,0);

        initAnim();//初始化动画

        setCurrentTime();//一进来就更新一下时间，保证是当前时间，其实感觉没什么用
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                 startY =  (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当用户按住头条新闻的viewpager进行下拉时，ACTION_DOWN会被viewpager消费掉，
                // 导致startY没有赋值，此处需要重新获取一下
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                if (mCurrentState == REFRESHING) {
                    //如果move时，状态为正在刷新，不要做任何处理，就是不要在改变它的状态了
                    break;
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;
                int padding = dy - viewHeight;//计算当前下拉控件的padding值
                //当前显示的第一个item的位置，仅当item为0，即头条新闻的时候，下拉才出现下拉刷新界面
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    view.setPadding(0,padding,0,0);

                    if (padding > 0 && mCurrentState != RELEASE_TO_REFRESH) {
                        // 改为松开刷新
                        mCurrentState = RELEASE_TO_REFRESH;
                        refresh();//每次改变状态都要进行刷新
                    } else if (padding < 0 && mCurrentState != PULL_TO_REFRESH) {
                        // 改为下拉刷新
                        mCurrentState = PULL_TO_REFRESH;
                        refresh();
                    }

                    return true;//表示下拉事件自己处理，
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;//重新给startY赋值
                if (mCurrentState == PULL_TO_REFRESH) {
                    // 隐藏头布局
                    view.setPadding(0,-viewHeight,0,0);
                } else if (mCurrentState == RELEASE_TO_REFRESH) {
                    mCurrentState = REFRESHING;
                    refresh();

                    // 4. 在需要刷新的时候，进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }

                    /*onRefresh方法，你的朋友就是通过它来问你要不要买，手机参数信息放在这里*/


                    // 完整展示头布局
                    view.setPadding(0,0,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    //出失火箭头动画
    public void initAnim() {
        animUP = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animUP.setDuration(300);
        animUP.setFillAfter(true);
        animDOWN = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animDOWN.setDuration(300);
        animDOWN.setFillAfter(true);
    }

    //根据当前状态刷新界面
    public void refresh() {
        switch (mCurrentState) {
            case PULL_TO_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.INVISIBLE);
                iv_arrow.startAnimation(animDOWN);
                break;
            case RELEASE_TO_REFRESH:
                tv_state.setText("松开刷新");
                iv_arrow.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.INVISIBLE);
                iv_arrow.startAnimation(animUP);
                break;
            case REFRESHING:
                tv_state.setText("正在刷新...");
                iv_arrow.clearAnimation();//先清除箭头动画，否则无法隐藏
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_loading.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 3. 定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    //  1. 设置下拉刷新的回调接口
    public interface OnRefreshListener{
        public void onRefresh();
    }

    //  2. 暴露接口，设置监听
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    // 刷新结束，收起头部控件 view ,并回复默认的 PULL_TO_REFRESH 状态
    // 它是在TabDetailPager中，执行刷新操作后去调用的
    public void onRefreshComplete(boolean success){
        view.setPadding(0,-viewHeight,0,0);
        mCurrentState = PULL_TO_REFRESH;
        tv_state.setText("下拉刷新");
        iv_arrow.setVisibility(View.VISIBLE);
        pb_loading.setVisibility(View.INVISIBLE);
        if (success) {
            setCurrentTime();//只有当刷新成功后才更新一下时间
        }
    }

    //  刷新后，更新时间，刷新成功：更新    刷新失败：不更新
    public void setCurrentTime() {
        // HH 表示24小时进制  hh 表示12小时进制   MM 表示从 1 开始，mm 表示从 0 开始
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());//获取当前时间
        tv_time.setText(time);
    }

}
