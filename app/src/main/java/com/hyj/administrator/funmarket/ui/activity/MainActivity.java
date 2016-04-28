package com.hyj.administrator.funmarket.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.ui.fragment.BaseFragment;
import com.hyj.administrator.funmarket.ui.fragment.FragmentFactory;
import com.hyj.administrator.funmarket.ui.view.PagerTab;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 当项目和appcompat关联在一起时, 就必须在清单文件中设置Theme.AppCompat的主题, 否则崩溃
 * android:theme="@style/Theme.AppCompat.Light"
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PagerTab mPagerTab;//指示器
    private ViewPager mViewPager;//ViewPager
    private MyAdapter mAdapter;//ViewPager的FragmentPagerAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPagerTab = (PagerTab) findViewById(R.id.pager_tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mPagerTab.setViewPager(mViewPager);// 将指针和viewpager绑定在一起

        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //页面滑动停止
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.createFragment(position);

                // 开始加载数据
                fragment.onLoadData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

//    FragmentPagerAdapter是PagerAdapter的子类, 如果viewpager的页面是fragment的话,就继承此类
    private class MyAdapter extends FragmentPagerAdapter {
        private String[] mTabNames;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UiUtil.getStringArray(R.array.tab_names);// 加载页面标题数组
        }

        // 返回页签标题


        @Override
        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);//方法里什么都没写所以可以删
            return mTabNames[position];
        }

        // 返回当前页面位置的fragment对象
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        // fragment数量
        @Override
        public int getCount() {
            return mTabNames.length;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
