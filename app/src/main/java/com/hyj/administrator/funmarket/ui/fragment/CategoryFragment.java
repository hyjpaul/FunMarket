package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;

import com.hyj.administrator.funmarket.domain.CategoryInfo;
import com.hyj.administrator.funmarket.http.protocol.CategoryProtocol;
import com.hyj.administrator.funmarket.ui.adapter.MyBaseAdapter;
import com.hyj.administrator.funmarket.ui.holder.CategoryHolder;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.ui.holder.TitleHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.ui.view.MyListView;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 分类
 */
public class CategoryFragment extends BaseFragment {
    private ArrayList<CategoryInfo> mData;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UiUtil.getContext());
        view.setAdapter(new MyAdapter(mData));
        return view;
    }

    @Override
    public LoadPage.ResultState onLoad() {
        CategoryProtocol protocol = new CategoryProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }

    private class MyAdapter extends MyBaseAdapter<CategoryInfo> {

        public MyAdapter(ArrayList<CategoryInfo> data) {
            super(data);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;// 在原来基础上增加一种标题类型
        }

        @Override
        public int getInnerType(int position) {
            // 判断是标题类型还是普通分类类型
            CategoryInfo info = mData.get(position);

            if (info.isTitle) {
                // 返回标题类型
                return super.getInnerType(position) + 1;// 原来类型基础上加1
            } else {
                // 返回普通类型
                return super.getInnerType(position);
            }
        }

        @Override
        public MyBaseHolder<CategoryInfo> getHolder(int position) {
            // 判断是标题类型还是普通分类类型, 来返回不同的holder
            CategoryInfo info = mData.get(position);

            if (info.isTitle) {
                return new TitleHolder();
            } else {
                return new CategoryHolder();
            }
        }

        @Override
        public boolean hasMore() {
            return false;// 没有更多数据, 需要隐藏加载更多的布局
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }
    }
}
