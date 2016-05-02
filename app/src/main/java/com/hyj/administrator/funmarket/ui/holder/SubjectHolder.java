package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.SubjectInfo;
import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.uiutils.BitmapHelper;
import com.hyj.administrator.funmarket.uiutils.UiUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * 专题holder
 */
public class SubjectHolder extends MyBaseHolder<SubjectInfo> {
    private ImageView ivPic;
    private TextView tvTitle;

    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.list_item_subject);
        ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        mBitmapUtils = BitmapHelper.getBitmapUtils();

        return view;
    }

    @Override
    protected void refreshView(SubjectInfo data) {
        tvTitle.setText(data.des);
        mBitmapUtils.display(ivPic, HttpHelper.URL + "image?name=" + data.url);
    }
}
