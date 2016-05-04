package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;

import com.hyj.administrator.funmarket.domain.SubjectInfo;
import com.hyj.administrator.funmarket.http.protocol.SubjectProtocol;
import com.hyj.administrator.funmarket.ui.adapter.MyBaseAdapter;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.ui.holder.SubjectHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.ui.view.MyListView;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 专题
 */
public class SubjectFragment extends BaseFragment {
    private ArrayList<SubjectInfo> mData;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UiUtil.getContext());
        view.setAdapter(new MyAdapter(mData));
        return view;
    }

    @Override
    public LoadPage.ResultState onLoad() {
        SubjectProtocol protocol = new SubjectProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }

    private class MyAdapter extends MyBaseAdapter<SubjectInfo> {
        public MyAdapter(ArrayList<SubjectInfo> data) {
            super(data);
        }

        @Override
        public MyBaseHolder<SubjectInfo> getHolder(int posotion) {
            return new SubjectHolder();
        }

        @Override
        public ArrayList<SubjectInfo> onLoadMore() {
            SubjectProtocol protocol = new SubjectProtocol();
            ArrayList<SubjectInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
