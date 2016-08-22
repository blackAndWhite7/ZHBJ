package com.project.wei.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public abstract class BaseFragment extends Fragment {

    private Activity mActivity; //这个activity就是MainActivity


    // Fragment创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    // 初始化fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        return view;
    }
    // fragment所依赖的activity的onCreate方法执行结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();// 初始化数据

    }
    // 初始化布局, 必须由子类实现
    public abstract View initView();
    // 初始化数据, 必须由子类实现
    public abstract void initData();
}
