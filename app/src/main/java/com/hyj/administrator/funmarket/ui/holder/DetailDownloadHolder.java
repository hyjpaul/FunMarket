package com.hyj.administrator.funmarket.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.domain.DownloadInfo;
import com.hyj.administrator.funmarket.manager.DownloadManager;
import com.hyj.administrator.funmarket.ui.view.ProgressHorizontal;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 详情页-下载
 */
public class DetailDownloadHolder extends MyBaseHolder<AppInfo> implements DownloadManager.DownloadObserver, View.OnClickListener {
    private DownloadManager mDownloadManager;

    private int mCurrentState;
    private float mProgress;

    private FrameLayout flProgress;
    private Button btnDownload;
    private ProgressHorizontal pbProgress;

    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.layout_detail_download);

        // 注册观察者, 监听状态和进度变化
        mDownloadManager = DownloadManager.getDownloadManager();
        mDownloadManager.registerObserver(this);

        // 初始化下载按钮
        btnDownload = (Button) view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);

        // 初始化自定义进度条
        flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        flProgress.setOnClickListener(this);

        pbProgress = new ProgressHorizontal(UiUtil.getContext());
        pbProgress.setProgressTextColor(Color.WHITE);// 进度文字颜色
        pbProgress.setProgressTextSize(UiUtil.dip2px(18));// 进度文字大小
        pbProgress.setBackgroundResource(R.drawable.progress_bg);// 进度条背景图片
        pbProgress.setProgressResource(R.drawable.progress_normal);// 进度条图片

        //进度条大小，占满父布局FrameLayout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        flProgress.addView(pbProgress, params);

        return view;
    }

    //DownloadObserver接口的方法，用于回调
    @Override
    protected void refreshView(AppInfo data) {
        // 判断当前应用是否下载过
        DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(data);

        if (downloadInfo != null) {
            // 之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.currentPos;
        } else {
            // 没有下载过
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }

        refreshUI(mCurrentState, mProgress);
    }

    // 根据当前的下载进度和状态来更新界面
    private void refreshUI(int currentState, float progress) {
        //System.out.println("刷新ui了:" + currentState);

        mCurrentState = currentState;
        mProgress = progress;

        switch (currentState) {
            case DownloadManager.STATE_UNDO:// 未下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载");
                break;

            case DownloadManager.STATE_WAITING:// 等待下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("等待中..");
                break;

            case DownloadManager.STATE_DOWNLOADING:// 正在下载
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("");
                pbProgress.setProgress(mProgress);// 设置下载进度
                break;

            case DownloadManager.STATE_PAUSE:// 下载暂停
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(mProgress);

               // System.out.println("暂停界面更新:" + mCurrentState);
                break;

            case DownloadManager.STATE_ERROR:// 下载失败
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载失败");
                break;

            case DownloadManager.STATE_SUCCESS:// 下载成功
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                break;

            default:
                break;
        }
    }

    // 在主线程更新ui
    private void RefreshUiOnMainThread(final DownloadInfo info) {
        // 判断下载对象是否是当前应用
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UiUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(info.currentState, info.getProgress());
                }
            });
        }

    }

    //状态更新，主、子线程都有执行
    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        RefreshUiOnMainThread(info);

    }

    // 进度更新, 子线程（DownloadTask的run方法里）
    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        RefreshUiOnMainThread(info);
    }

    //进度条和下载按钮点击事件
    @Override
    public void onClick(View v) {
        //System.out.println("点击事件响应了:" + mCurrentState);

        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.fl_progress:
// 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO ||
                        mCurrentState == DownloadManager.STATE_PAUSE ||
                        mCurrentState == DownloadManager.STATE_ERROR) {
                    mDownloadManager.download(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_DOWNLOADING ||
                        mCurrentState == DownloadManager.STATE_WAITING) {
                    mDownloadManager.pause(getData());// 暂停下载
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    mDownloadManager.install(getData());// 开始安装
                }
                break;
            default:
                break;
        }
    }
}
