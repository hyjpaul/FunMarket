package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {
    // 如果加载数据成功, 就回调此方法, 在主线程运行
    @Override
    public View onCreateSuccessView() {
        TextView view = new TextView(UiUtil.getContext());
        view.setText(getClass().getSimpleName());
        return view;
    }

    // 运行在子线程,可以直接执行耗时网络操作(LoadPage的loadData()的new Thread()里调用)
    @Override
    public LoadPage.ResultState onLoad() {
// 请求网络
        return LoadPage.ResultState.STATE_SUCCESS;
    }
}
