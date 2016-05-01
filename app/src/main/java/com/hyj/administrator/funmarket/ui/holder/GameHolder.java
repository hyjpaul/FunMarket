package com.hyj.administrator.funmarket.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.uiutils.BitmapHelper;
import com.hyj.administrator.funmarket.uiutils.UiUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * 游戏holder
 */
public class GameHolder extends MyBaseHolder<AppInfo> {

    private TextView tvName, tvSize, tvDes;
    private ImageView ivIcon;
    private RatingBar rbStar;

    private BitmapUtils mBitmapUtils;

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

        //mBitmapUtils = new BitmapUtils(UIUtils.getContext());
        mBitmapUtils = BitmapHelper.getBitmapUtils();

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UiUtil.getContext(), data.size));
        tvDes.setText(data.des);
        rbStar.setRating(data.stars);

        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
    }
}
