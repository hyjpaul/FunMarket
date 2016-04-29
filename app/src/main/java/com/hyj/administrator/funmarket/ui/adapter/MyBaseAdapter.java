package com.hyj.administrator.funmarket.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;

import java.util.ArrayList;

/**
 * 因为每个fragment页面都需要ListView，而ListView都要用到BaseAdapter，避免重复代码所以对adapter进行封装
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> mData;

    public MyBaseAdapter(ArrayList<T> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBaseHolder holder;
        if (convertView == null) {
            // 1. 加载布局文件
            // 2. 初始化控件 findViewById
            // 3. 打一个标记tag
            holder = getHolder();
        } else {
            holder = (MyBaseHolder) convertView.getTag();
        }

        // 4. 根据数据来刷新界面
        holder.setData(getItem(position));

        return holder.getRootView();

    }

    // 返回当前页面的holder对象, 必须子类实现
    public abstract MyBaseHolder<T> getHolder();
}
