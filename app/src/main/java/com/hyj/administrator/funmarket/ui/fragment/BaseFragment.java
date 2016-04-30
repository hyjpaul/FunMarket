package com.hyj.administrator.funmarket.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * Fragment的一个基类，共性
 */
public abstract class BaseFragment extends Fragment {

    private LoadPage mLoadPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////         使用textview显示当前类的类名
//         TextView view = new TextView(UiUtil.getContext());
//         view.setText(getClass().getSimpleName());

        mLoadPage = new LoadPage(UiUtil.getContext()) {
            @Override
            public View onCreateSuccessView() {
                // 注意:此处一定要调用BaseFragment的onCreateSuccessView, 否则栈溢出(同名直接return onCreateSuccessView() 自己调自己)
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return mLoadPage;
    }

    // 加载成功的布局, 必须由子类Fragment来实现各自的不同布局
    public abstract View onCreateSuccessView();

    // 加载网络数据, 必须由子类来实现
    public abstract LoadPage.ResultState onLoad();

    // 开始加载数据
    public void onLoadData() {
        if (mLoadPage != null) {
            mLoadPage.loadData();
        }
    }

    // 对网络返回数据的合法性进行校验
    public LoadPage.ResultState check(Object obj) {
        if (obj != null) {
            if (obj instanceof ArrayList) {// 判断是否是集合
                ArrayList list = (ArrayList) obj;

                if (list.isEmpty()) {
                    return LoadPage.ResultState.STATE_EMPTY;
                } else {
                    return LoadPage.ResultState.STATE_SUCCESS;
                }
            }

        }
        return LoadPage.ResultState.STATE_ERROR;
    }
}
