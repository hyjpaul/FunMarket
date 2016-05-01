package com.hyj.administrator.funmarket.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 每个页面都会用到所以封装一下防代码重复
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        initView();
    }


    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setDivider(null);// 去掉分隔线
        this.setCacheColorHint(Color.TRANSPARENT);// 有时候滑动listview背景会变成黑色,此方法将背景变为全透明
        this.setSelector(new ColorDrawable());// 设置默认状态选择器,没有参数为无颜色，相当于取消了ListView默认点击的背景边框效果
    }
}
