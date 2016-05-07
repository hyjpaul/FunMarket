package com.hyj.administrator.funmarket.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.protocol.HomeDetailProtocol;
import com.hyj.administrator.funmarket.ui.holder.DetailAppInfoHolder;
import com.hyj.administrator.funmarket.ui.holder.DetailSafeHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 首页应用详情页
 */
public class HomeDetailActivity extends BaseActivity {

    private LoadPage mLoadPage;
    private String packageName;
    private AppInfo mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadPage = new LoadPage(this) {
            @Override
            public View onCreateSuccessView() {
                return HomeDetailActivity.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return HomeDetailActivity.this.onLoad();
            }
        };

        // setContentView(R.layout.activity_main);
        setContentView(mLoadPage);// 直接将一个view对象设置给activity

        // 获取从HomeFragment传递过来的包名
        packageName = getIntent().getStringExtra("packageName");

        // 开始加载网络数据
        mLoadPage.loadData();
    }

    public View onCreateSuccessView() {
        // 初始化成功的布局
        View view = UiUtil.inflateView(R.layout.page_home_detail);

        // 初始化应用信息模块
        FrameLayout flDetailAppInfo = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        DetailAppInfoHolder appInfoHolder = new DetailAppInfoHolder();
        flDetailAppInfo.addView(appInfoHolder.getRootView());
        appInfoHolder.setData(mData);

        // 初始化安全描述模块
        FrameLayout flDetailSafe = (FrameLayout) view.findViewById(R.id.fl_detail_safe);
        DetailSafeHolder safeHolder = new DetailSafeHolder();
        flDetailSafe.addView(safeHolder.getRootView());
        safeHolder.setData(mData);

        return view;
    }

    public LoadPage.ResultState onLoad() {
        // 请求网络,加载数据
        HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
        mData = protocol.getData(0);

        if (mData != null) {
            return LoadPage.ResultState.STATE_SUCCESS;
        } else {
            return LoadPage.ResultState.STATE_ERROR;
        }
    }
}
