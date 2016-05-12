package com.hyj.administrator.funmarket.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.domain.DownloadInfo;
import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.manager.DownloadManager;
import com.hyj.administrator.funmarket.ui.view.ProgressArc;
import com.hyj.administrator.funmarket.uiutils.BitmapHelper;
import com.hyj.administrator.funmarket.uiutils.UiUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * 首页holder
 */
public class HomeHolder extends MyBaseHolder<AppInfo> implements DownloadManager.DownloadObserver, View.OnClickListener {

    private TextView tvName, tvSize, tvDes;
    private ImageView ivIcon;
    private RatingBar rbStar;

    private BitmapUtils mBitmapUtils;

    private DownloadManager mDownloadManager;

    private ProgressArc pbProgress;

    private int mCurrentState;
    private float mProgress;

    private TextView tvDownload;

    @Override
    public View initView() {
//        // 1. 加载布局
//        View view = UiUtil.inflateView(R.layout.list_item_home);
//        // 2. 初始化控件
//        tvContent = (TextView) view.findViewById(R.id.tv_content);

// 1. 加载布局
        View view = UiUtil.inflateView(R.layout.list_item_home);
        // 2. 初始化控件
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        tvDes = (TextView) view.findViewById(R.id.tv_des);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);

        tvDownload = (TextView) view.findViewById(R.id.tv_download);

        //mBitmapUtils = new BitmapUtils(UIUtils.getContext());
        mBitmapUtils = BitmapHelper.getBitmapUtils();

        // 初始化进度条
        FrameLayout flProgress = (FrameLayout) view
                .findViewById(R.id.fl_progress);
        flProgress.setOnClickListener(this);

        pbProgress = new ProgressArc(UiUtil.getContext());
        // 设置圆形进度条直径
        pbProgress.setArcDiameter(UiUtil.dip2px(26));
        // 设置进度条颜色
        pbProgress.setProgressColor(UiUtil.getColor(R.color.progress));
        // 设置进度条宽高布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UiUtil.dip2px(27), UiUtil.dip2px(27));
        flProgress.addView(pbProgress, params);

        mDownloadManager = DownloadManager.getDownloadManager();
        mDownloadManager.registerObserver(this);// 注册观察者, 监听状态和进度变化

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UiUtil.getContext(), data.size));
        tvDes.setText(data.des);
        rbStar.setRating(data.stars);

        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);

        // 判断当前应用是否下载过
        DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(data);
        if (downloadInfo != null) {
            // 之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getProgress();
        } else {
            // 没有下载过
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }

        refreshUI(mCurrentState, mProgress, data.id);
    }

    //    刷新界面
    private void refreshUI(int currentState, float progress, String id) {
        // 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
        if (!getData().id.equals(id)) {
            return;
        }

        mCurrentState = currentState;
        mProgress = progress;

        switch (currentState) {
            case DownloadManager.STATE_UNDO:
                // 自定义进度条背景
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                // 没有进度
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAITING:
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                // 等待模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText("等待");
                break;
            case DownloadManager.STATE_DOWNLOADING:
                pbProgress.setBackgroundResource(R.drawable.ic_pause);
                // 下载中模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                pbProgress.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case DownloadManager.STATE_PAUSE:
                pbProgress.setBackgroundResource(R.drawable.ic_resume);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case DownloadManager.STATE_ERROR:
                pbProgress.setBackgroundResource(R.drawable.ic_redownload);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS:
                pbProgress.setBackgroundResource(R.drawable.ic_install);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("安装");
                break;

            default:
                break;
        }
    }

    // 主线程更新ui
    private void refreshUIOnMainThread(final DownloadInfo info) {
        // 判断下载对象是否是当前应用
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UiUtil.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refreshUI(info.currentState, info.getProgress(), info.id);
                }
            });
        }
    }

    //主页和应用详情页都实现观察者接口，都是观察者，所以点击下载进入这个方法两个页面都能看到同时的下载进度条
    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_progress:
                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_ERROR
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    mDownloadManager.download(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
                        || mCurrentState == DownloadManager.STATE_WAITING) {
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
