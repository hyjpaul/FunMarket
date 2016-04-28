package com.hyj.administrator.funmarket.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 根据当前状态来显示不同页面的自定义控件
 * <p>
 * - 未加载 - 加载中 - 加载失败 - 数据为空 - 加载成功  FrameLayout addView不同状态的布局
 */
public abstract class LoadPage extends FrameLayout {

    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_EMPTY = 4;// 数据为空
    private static final int STATE_LOAD_SUCCESS = 5;// 加载成功

    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态

    private View mLodingPage;//加载中布局
    private View mErrorPage;//加载失败布局
    private View mEmptyPage;//加载数据为空布局
    private View mSuccessPage;//加载成功布局

    public LoadPage(Context context) {
        super(context);
        initView();
    }


    public LoadPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public LoadPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始化加载中的布局
        if (mLodingPage == null) {
            mLodingPage = UiUtil.inflateView(R.layout.page_loding);

            addView(mLodingPage);// 将加载中的布局添加给当前的帧布局
        }

        // 初始化加载失败布局
        if (mErrorPage == null) {
            mErrorPage = UiUtil.inflateView(R.layout.page_error);

            addView(mErrorPage);
        }

        // 初始化数据为空布局
        if (mEmptyPage == null) {
            mEmptyPage = UiUtil.inflateView(R.layout.page_empty);

            addView(mEmptyPage);
        }

        showStatePage();
    }


    // 根据当前状态,决定显示哪个布局
    private void showStatePage() {
        // if (mCurrentState == STATE_LOAD_UNDO
        // || mCurrentState == STATE_LOAD_LOADING) {
        // mLoadingPage.setVisibility(View.VISIBLE);
        // } else {
        // mLoadingPage.setVisibility(View.GONE);
        // }
        mLodingPage.setVisibility((mCurrentState == STATE_LOAD_UNDO
                || mCurrentState == STATE_LOAD_LOADING) ? View.VISIBLE : View.GONE);

        mErrorPage
                .setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE
                        : View.GONE);

        mEmptyPage
                .setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE
                        : View.GONE);

        // 当成功布局为空,并且当前状态为成功,才初始化成功的布局
        if (mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessPage = onCreateSuccessView();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }

        if (mSuccessPage != null) {
            mSuccessPage
                    .setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE
                            : View.GONE);
        }

    }

    // 开始加载数据
    public void loadData() {
        if (mCurrentState != STATE_LOAD_LOADING) {// 如果当前没有加载, 就开始加载数据
            mCurrentState = STATE_LOAD_LOADING;

            new Thread() {
                @Override
                public void run() {
                    final ResultState resultState = onLoad();

                    // 运行在主线程
                    UiUtil.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultState != null) {
                                mCurrentState = resultState.getState();// 网络加载结束后,更新网络状态
                                // 根据最新的状态来刷新页面
                                showStatePage();
                            }
                        }
                    });
                }


            }.start();

        }

    }

    // 加载成功后显示的布局, 必须由调用者来实现,因为每个页面的成功布局是不一样的
    public abstract View onCreateSuccessView();

    // 加载网络数据, 返回值表示请求网络结束后的状态
    public abstract ResultState onLoad();

    //定义带有状态参数的枚举类方便子fragment返回状态
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_ERROR(
                STATE_LOAD_ERROR);

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

//    public static class Person {
//
//        public static Person P1 = new Person(10);
//        public static Person P2 = new Person(12);
//        public static Person P3 = new Person(19);
//
//        public Person(int age) {
//
//        }
//    }
//
//    // public enum Person {
//    // P1,P2,P3;
//    // }

}
