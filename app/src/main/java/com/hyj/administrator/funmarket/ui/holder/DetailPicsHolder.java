package com.hyj.administrator.funmarket.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.uiutils.BitmapHelper;
import com.hyj.administrator.funmarket.uiutils.UiUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 首页详情页-截图
 */
public class DetailPicsHolder extends MyBaseHolder<AppInfo> {
    private ImageView[] ivPics;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.layout_detail_picinfo);

        ivPics = new ImageView[5];
        ivPics[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        ivPics[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        ivPics[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        ivPics[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        ivPics[4] = (ImageView) view.findViewById(R.id.iv_pic5);

        mBitmapUtils = BitmapHelper.getBitmapUtils();

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {
        final ArrayList<String> screen = data.screen;

        for (int i = 0; i < 5; i++) {
            if (i < screen.size()) {
                mBitmapUtils.display(ivPics[i], HttpHelper.URL + "image?name="
                        + screen.get(i));
            } else {
                ivPics[i].setVisibility(View.GONE);
            }
        }

    }
}
