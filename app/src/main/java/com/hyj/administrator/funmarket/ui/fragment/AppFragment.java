package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;

import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.protocol.AppProtocol;
import com.hyj.administrator.funmarket.ui.adapter.MyBaseAdapter;
import com.hyj.administrator.funmarket.ui.holder.AppHolder;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.ui.view.MyListView;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> mData;// 加载第一页数据

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UiUtil.getContext());
        view.setAdapter(new MyAdapter(mData));
        return view;
    }

    @Override
    public LoadPage.ResultState onLoad() {
        AppProtocol protocol = new AppProtocol();
        mData = protocol.getData(0);

        return check(mData);
    }

    private class MyAdapter extends  MyBaseAdapter {

        public MyAdapter(ArrayList data) {
            super(data);
        }

        @Override
        public MyBaseHolder getHolder() {
            return new AppHolder();
        }

        @Override
        public ArrayList onLoadMore() {
            AppProtocol protocol = new AppProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
