package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 首页holder
 */
public class HomeHolder extends MyBaseHolder<String> {

    private TextView tvContent;

    @Override
    public View initView() {
        // 1. 加载布局
        View view = UiUtil.inflateView(R.layout.list_item_home);
        // 2. 初始化控件
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        return view;
    }

    @Override
    protected void refreshView(String data) {
        tvContent.setText(data);
    }
}
