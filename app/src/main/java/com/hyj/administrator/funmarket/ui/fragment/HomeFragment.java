package com.hyj.administrator.funmarket.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.hyj.administrator.funmarket.ui.adapter.MyBaseAdapter;
import com.hyj.administrator.funmarket.ui.holder.HomeHolder;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.ui.view.LoadPage;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private ArrayList mData;

    // 如果加载数据成功, 就回调此方法, 在主线程运行
    @Override
    public View onCreateSuccessView() {
//        TextView view = new TextView(UiUtil.getContext());
//        view.setText(getClass().getSimpleName());

        ListView view = new ListView(UiUtil.getContext());

        view.setAdapter(new MyAdapter(mData));
        return view;
    }

    // 运行在子线程,可以直接执行耗时网络操作(LoadPage的loadData()的new Thread()里调用)
    @Override
    public LoadPage.ResultState onLoad() {
// 请求网络
        mData = new ArrayList();

        for (int i = 0; i < 30; i++) {
            mData.add("测试：" + i);
        }


        return LoadPage.ResultState.STATE_SUCCESS;
    }

    //ListView适配器
    private class MyAdapter extends MyBaseAdapter {
        public MyAdapter(ArrayList data) {
            super(data);
        }

        @Override
        public MyBaseHolder getHolder() {
            return new HomeHolder();
        }

        //对ListView进行第一次封装：MyBaseAdapter 泛型getCount() getItem(int position) getItemId(int position)
//        @Override
//        public int getCount() {
//            return mData.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
        //对ListView进行第二次封装：MyBaseHolder：加载ListView的item布局, 初始化控件,设置tag，根据数据刷新布局
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            // 1. 加载布局文件
//            if (convertView == null) {
//                convertView = UiUtil.inflateView(R.layout.list_item_home);
//// 2. 初始化控件 findViewById
//                holder = new ViewHolder();
//                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
//                // 3. 打一个标记tag
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//// 4. 根据数据来刷新界面
//            String content = (String) getItem(position);
//            holder.tvContent.setText(content);
//
//            return convertView;

//        }

    }

//    public static class ViewHolder {
//        public TextView tvContent;
//    }
}
