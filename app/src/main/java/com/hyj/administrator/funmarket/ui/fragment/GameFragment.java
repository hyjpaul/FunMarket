package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;

import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.http.protocol.GameProtocol;
import com.hyj.administrator.funmarket.ui.adapter.MyBaseAdapter;
import com.hyj.administrator.funmarket.ui.holder.GameHolder;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.ui.view.MyListView;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 游戏
 */
public class GameFragment extends BaseFragment {
    private ArrayList<AppInfo> mData;// 加载第一页数据

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UiUtil.getContext());
        view.setAdapter(new MyAdapter(mData));
        return view;
    }

    @Override
    public LoadPage.ResultState onLoad() {
        GameProtocol protocol = new GameProtocol();
        mData = protocol.getData(0);

        return check(mData);
    }

    private class MyAdapter extends MyBaseAdapter {

        public MyAdapter(ArrayList data) {
            super(data);
        }

        @Override
        public MyBaseHolder getHolder(int posotion) {
            return new GameHolder();
        }

        @Override
        public ArrayList onLoadMore() {
            GameProtocol protocol = new GameProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
