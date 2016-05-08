package com.hyj.administrator.funmarket.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyj.administrator.funmarket.manager.ThreadManager;
import com.hyj.administrator.funmarket.ui.holder.MoreHolder;
import com.hyj.administrator.funmarket.ui.holder.MyBaseHolder;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

import java.util.ArrayList;

/**
 * 因为每个fragment页面都需要ListView，而ListView都要用到BaseAdapter，避免重复代码所以对adapter进行封装
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    //注意: 此处必须要从0开始写,因为值是要传给getItemViewType(int position)这方法底层是从0开始的数组
    private static final int TYPE_NORMAL = 1;// 正常布局类型
    private static final int TYPE_MORE = 0;// 加载更多类型


    private ArrayList<T> mData;//开始是第一页数据，后面会addAll加载更多的数据

    public MyBaseAdapter(ArrayList<T> data) {
        mData = data;
    }

    // 返回布局类型个数
    @Override
    public int getViewTypeCount() {
        return 2;// 返回两种类型,普通布局+加载更多布局

    }

    // 返回当前位置应该展示那种布局类型
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {// 最后一个
            return TYPE_MORE;//确定的：最后就是加载更多的布局
        } else {
            return getInnerType(position);//布局不确定，子类可重写
        }

    }

    // 子类可以重写此方法来更改返回的布局类型
    public int getInnerType(int position) {
        return TYPE_NORMAL;// 默认就是普通类型
    }

    @Override
    public int getCount() {
        return mData.size() + 1;// 增加加载更多布局数量
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBaseHolder holder;
        if (convertView == null) {
            // 1. 加载布局文件
            // 2. 初始化控件 findViewById
            // 3. 打一个标记tag
            if (getItemViewType(position) == TYPE_MORE) {
                // 加载更多的类型
                holder = new MoreHolder(hasMore());
            } else {

                holder = getHolder(position);// 子类返回具体holder对象
            }
        } else {
            holder = (MyBaseHolder) convertView.getTag();
        }

        // 4. 根据数据来刷新界面
        if (getItemViewType(position) != TYPE_MORE) {

            holder.setData(getItem(position));
        } else {
            // 加载更多布局
            // 一旦加载更多布局展示出来, 就开始加载更多
            // 只有在有更多数据的状态下才加载更多
            //ListView展现item一定会掉getView方法，这里是根据展现出了加载更多的布局就代表可以加载更多数据了，还有一种思路是在HomeFragment的new ListView那里设一个滚动监听到最后一个可见item然后加载更多数据
            MoreHolder moreHolder = (MoreHolder) holder;
            if (moreHolder.getData() == MoreHolder.STATE_MORE_MORE) {
                loadMore(moreHolder);
            }
        }

        return holder.getRootView();

    }


    // 返回当前页面的holder对象, 必须子类实现
    public abstract MyBaseHolder<T> getHolder(int position);

    // 子类可以重写此方法来决定是否可以加载更多
    public boolean hasMore() {
        return true;// 默认都是有更多数据的
    }

    private boolean isLoadMore = false;//标记是否正在加载更多

    // 加载更多数据
    public void loadMore(final MoreHolder holder) {
        if (!isLoadMore) {//没有正在加载更多才去加载
            isLoadMore = true;

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    final ArrayList<T> moreData = onLoadMore();//子类获取到的数据
//
//                    UiUtil.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (moreData != null) {
//                                // 本app用的服务器MyWebServer返回的每一页就只有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
//                                if (moreData.size() < 20) {
//                                    holder.setData(MoreHolder.STATE_MORE_NONE);
//                                    Toast.makeText(UiUtil.getContext(),
//                                            "没有更多数据了", Toast.LENGTH_SHORT)
//                                            .show();
//                                } else {
//                                    // 还有更多数据
//                                    holder.setData(MoreHolder.STATE_MORE_MORE);
//                                }
//
//                                // 将更多数据追加到当前集合中
//                                mData.addAll(moreData);
//                                // 通知刷新界面
//                                MyBaseAdapter.this.notifyDataSetChanged();
//
//                            } else {
//                                // 加载更多失败
//                                holder.setData(MoreHolder.STATE_MORE_ERROR);
//                            }
//
//                            isLoadMore = false;//加载更多完毕
//                        }
//                    });
//                }
//            }).start();

            //用线程池
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();//子类获取到的数据

                    UiUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (moreData != null) {
                                // 本app用的服务器MyWebServer返回的每一页就只有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
                                if (moreData.size() < 20) {
                                    holder.setData(MoreHolder.STATE_MORE_NONE);
                                    Toast.makeText(UiUtil.getContext(),
                                            "没有更多数据了", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    // 还有更多数据
                                    holder.setData(MoreHolder.STATE_MORE_MORE);
                                }

                                // 将更多数据追加到当前集合中
                                mData.addAll(moreData);
                                // 通知刷新界面
                                MyBaseAdapter.this.notifyDataSetChanged();

                            } else {
                                // 加载更多失败
                                holder.setData(MoreHolder.STATE_MORE_ERROR);
                            }

                            isLoadMore = false;//加载更多完毕
                        }
                    });
                }
            });
        }
    }

    // 具体去加载更多数据, 必须由子类实现
    public abstract ArrayList<T> onLoadMore();

    //获取当前集合大小
    public int getListSize() {
        return mData.size();
    }


}
