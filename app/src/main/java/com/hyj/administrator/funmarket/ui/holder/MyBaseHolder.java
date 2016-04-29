package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;

/**
 *
 */
public abstract class MyBaseHolder<T> {
    private View mRootView;// 一个item的根布局
    private T mData;

    //当new这个对象时, 就会加载ListView的item布局, 初始化控件,设置tag
    public MyBaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);
    }

    // 1. 加载布局文件
    // 2. 初始化控件 findViewById
    public abstract View initView();

    // 返回item的布局对象
    public View getRootView() {
        return mRootView;
    }

    // 设置当前item的数据
    public void setData(T data) {
        mData = data;
        //一得到数据就可以刷新页面了
        refreshView(data);
    }

    // 获取当前item的数据
    public T getData() {
        return mData;
    }

    // 4. 根据数据来刷新界面
    protected abstract void refreshView(T data);
}
