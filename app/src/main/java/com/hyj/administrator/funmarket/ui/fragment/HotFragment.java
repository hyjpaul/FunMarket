package com.hyj.administrator.funmarket.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyj.administrator.funmarket.http.protocol.HotProtocol;
import com.hyj.administrator.funmarket.ui.view.FlowLayout;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.uiutils.DrawableUtil;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * 排行
 */
public class HotFragment extends BaseFragment {
    private ArrayList<String> mData;

    @Override
    public View onCreateSuccessView() {
        // 支持上下滑动
        ScrollView scrollView = new ScrollView(UiUtil.getContext());
        FlowLayout flow = new FlowLayout(UiUtil.getContext());

        int padding = UiUtil.dip2px(10);
        flow.setPadding(padding, padding, padding, padding);// 设置内边距
        flow.setHorizontalSpacing(UiUtil.dip2px(6));// 水平间距
        flow.setVerticalSpacing(UiUtil.dip2px(8));// 竖直间距

        for (int i = 0; i < mData.size(); i++) {
            final String keyword = mData.get(i);
            TextView view = new TextView(UiUtil.getContext());
            view.setText(keyword);
            view.setTextColor(Color.WHITE);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);// 18sp
            view.setPadding(padding, padding, padding, padding);
            view.setGravity(Gravity.CENTER);

            // 生成随机颜色
            Random random = new Random();
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);

            int color = 0xffcecece;// TextView按下后偏白的背景色

            // GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(
            // Color.rgb(r, g, b), UIUtils.dip2px(6));
            // GradientDrawable bgPress = DrawableUtils.getGradientDrawable(
            // color, UIUtils.dip2px(6));
            // StateListDrawable selector = DrawableUtils.getSelector(bgNormal,
            // bgPress);

            StateListDrawable selector = DrawableUtil.getSelector(Color.rgb(r, g, b), color, UiUtil.dip2px(6));
            view.setBackground(selector);

            flow.addView(view);

            // TextView默认是不能点击的,只有设置点击事件, 状态选择器才起作用
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(UiUtil.getContext(), keyword, Toast.LENGTH_SHORT).show();
                }
            });
        }

        scrollView.addView(flow);
        return scrollView;

    }

    @Override
    public LoadPage.ResultState onLoad() {
        HotProtocol protocol = new HotProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }
}
