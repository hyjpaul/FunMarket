package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 详情页-下载
 */
public class DetailDownloadHolder extends MyBaseHolder<AppInfo> {
    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.layout_detail_download);

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {

    }
}
