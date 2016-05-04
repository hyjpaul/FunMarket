package com.hyj.administrator.funmarket.ui.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.http.HttpHelper;
import com.hyj.administrator.funmarket.ui.view.fly.RandomLayout;
import com.hyj.administrator.funmarket.uiutils.BitmapHelper;
import com.hyj.administrator.funmarket.uiutils.UiUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 首页轮播条holder  纯代码实现布局
 */
public class HomeHeaderHolder extends MyBaseHolder<ArrayList<String>> {

    private ArrayList<String> mData;

    private ViewPager mViewPager;

    private LinearLayout llContainer;

    private int mPreviousPos;// 上个圆点位置

    @Override
    public View initView() {
        // 创建根布局, 相对布局
        RelativeLayout rlRoot = new RelativeLayout(UiUtil.getContext());
        // 初始化布局参数, 根布局上层控件是listview, 所以要使用listview定义的LayoutParams
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.dip2px(150));
        rlRoot.setLayoutParams(params);

        // ViewPager
        mViewPager = new ViewPager(UiUtil.getContext());
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // mViewPager.setLayoutParams(vpParams);
        rlRoot.addView(mViewPager);// 把viewpager添加给相对布局

        // 初始化指示器
        llContainer = new LinearLayout(UiUtil.getContext());
        llContainer.setOrientation(LinearLayout.HORIZONTAL);// 水平方向

        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RandomLayout.LayoutParams.WRAP_CONTENT);

        // 设置内边距
        int padding = UiUtil.dip2px(10);
        llContainer.setPadding(padding, padding, padding, padding);

        // 添加规则, 设定展示位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 底部对齐
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// 右对齐

        // 添加布局
        rlRoot.addView(llContainer, llParams);

        return rlRoot;
    }

    @Override
    protected void refreshView(ArrayList<String> data) {
        mData = data;
        // 填充viewpager的数据
        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setCurrentItem(mData.size() * 10000);

        // 初始化指示器
        for (int i = 0; i < mData.size(); i++) {
            ImageView point = new ImageView(UiUtil.getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i == 0) {// 第一个默认选中
                point.setImageResource(R.drawable.indicator_selected);
            } else {
                point.setImageResource(R.drawable.indicator_normal);

                params.leftMargin = UiUtil.dip2px(4);// 左边距
            }

            point.setLayoutParams(params);

            llContainer.addView(point);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mData.size();

                // 当前点被选中
                ImageView point = (ImageView) llContainer.getChildAt(position);
                point.setImageResource(R.drawable.indicator_selected);

                // 上个点变为不选中
                ImageView prePoint = (ImageView) llContainer
                        .getChildAt(mPreviousPos);
                prePoint.setImageResource(R.drawable.indicator_normal);

                mPreviousPos = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // UiUtil.getHandler().postDelayed(Runnable, delayMillis),也可sendEmptyMessage然后在handleMessage更新UI
        //启动轮播条自动播放
        HomeHeaderTask task = new HomeHeaderTask();
        task.start();

    }

    private class HomeHeaderTask implements Runnable {

        public void start() {
            // 移除之前发送的所有消息, 避免消息重复
            UiUtil.getHandler().removeCallbacksAndMessages(null);
            UiUtil.getHandler().postDelayed(this, 3000);
        }
        @Override
        public void run() {
            //更新ui，指示器小圆点往后移
            int currentItem = mViewPager.getCurrentItem();
            currentItem++;
            mViewPager.setCurrentItem(currentItem);

            // 继续发延时3秒消息, 实现内循环
            UiUtil.getHandler().postDelayed(this, 3000);
        }
    }

    private class MyAdapter extends PagerAdapter {
        private BitmapUtils mBitmapUtils;

        public MyAdapter() {
            mBitmapUtils = BitmapHelper.getBitmapUtils();
        }

        @Override
        public int getCount() {
            // return mData.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mData.size();

            String url = mData.get(position);

            ImageView imgView = new ImageView(UiUtil.getContext());
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);

            mBitmapUtils.display(imgView, HttpHelper.URL + "image?name=" + url);

            container.addView(imgView);
            return imgView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
