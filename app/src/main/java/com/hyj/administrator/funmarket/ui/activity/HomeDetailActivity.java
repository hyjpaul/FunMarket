package com.hyj.administrator.funmarket.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.protocol.HomeDetailProtocol;
import com.hyj.administrator.funmarket.ui.holder.DetailAppInfoHolder;
import com.hyj.administrator.funmarket.ui.holder.DetailDesHolder;
import com.hyj.administrator.funmarket.ui.holder.DetailPicsHolder;
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

        //设置toolbar返回键
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // 初始化截图模块
        HorizontalScrollView hsvPic = (HorizontalScrollView) view
                .findViewById(R.id.hsv_detail_pics);
        DetailPicsHolder picsHolder = new DetailPicsHolder();
        hsvPic.addView(picsHolder.getRootView());
        picsHolder.setData(mData);

        //初始化描述模块
        FrameLayout flDetailDes = (FrameLayout) view
                .findViewById(R.id.fl_detail_des);
        DetailDesHolder desHolder = new DetailDesHolder();
        flDetailDes.addView(desHolder.getRootView());
        desHolder.setData(mData);

        // getIntent().getSerializableExtra("list");如果要点击截图放大

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

    //点击ToolBar返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
