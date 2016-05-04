package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.CategoryInfo;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 分类的标题holder
 */
public class TitleHolder extends MyBaseHolder<CategoryInfo> {

    public TextView tvTitle;

    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.list_item_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    protected void refreshView(CategoryInfo data) {
        tvTitle.setText(data.title);
    }
}
