package com.hyj.administrator.funmarket.ui.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyj.administrator.funmarket.R;
import com.hyj.administrator.funmarket.domain.AppInfo;
import com.hyj.administrator.funmarket.uiutils.UiUtil;

/**
 * 首页详情页-应用描述
 */
public class DetailDesHolder extends MyBaseHolder<AppInfo> {
    private TextView tvDes;
    private TextView tvAuthor;
    private ImageView ivArrow;
    private RelativeLayout rlToggle;

    @Override
    public View initView() {
        View view = UiUtil.inflateView(R.layout.layout_detail_descinfo);

        tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
        tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);

        rlToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private boolean isOpen = false;
    private LinearLayout.LayoutParams mParams;

    private void toggle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();

        ValueAnimator animator = null;
        if (isOpen) {
            // 关闭
            isOpen = false;
            if (longHeight > shortHeight) {// 只有描述信息大于7行,才启动动画,因为如果描述信息可能小于7行shor和long就相等了
                animator = ValueAnimator.ofInt(longHeight, shortHeight);
            }
        } else {
            //打开
            isOpen = true;
            if (longHeight > shortHeight) {
                animator = ValueAnimator.ofInt(shortHeight, longHeight);
            }
        }
        if (animator != null) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer height = (Integer) animation.getAnimatedValue();
                    mParams.height = height;
                    tvDes.setLayoutParams(mParams);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //动画结束，ScrollView自动滑动到最底部
                    final ScrollView scrollView = getScrollView();

                    // 为了运行更加安全和稳定, 可以将滑动到底部方法放在消息队列中执行
                    scrollView.post(new Runnable() {//也可是Handler.post
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底部

                        }
                    });

                    if (isOpen) {
                        ivArrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        ivArrow.setImageResource(R.drawable.arrow_down);
                    }

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            animator.setDuration(200);
            animator.start();
        }
    }


    //获取7行textview的高度
    private int getShortHeight() {
        // 因为不知道应用描述文字的TextView的高度，所以模拟一个textview,设置最大行数为7行, 计算该虚拟textview的高度, 从而知道tvDes在展示7行时应该多高
        int width = tvDes.getMeasuredWidth(); // 布局中textView的宽度
        TextView view = new TextView(UiUtil.getContext());
        view.setText(getData().des);// 设置文字
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 文字大小一致14sp
        view.setMaxLines(7);// 最大行数为7行

//        view.measure(0,0);此时不能让系统自己去测，因为不是写在布局文件里的
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);// 宽不变, 确定值, match_parent
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);// 高度包裹内容, wrap_content;当包裹内容时不确定具体高度,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度

        // 开始测量
        view.measure(widthMeasureSpec, heightMeasureSpec);

        return view.getMeasuredHeight();// 返回测量后的高度
    }

    //    获取完整textview的高度
    private int getLongHeight() {

        int width = tvDes.getMeasuredWidth(); // 布局中textView的宽度
        TextView view = new TextView(UiUtil.getContext());
        view.setText(getData().des);// 设置文字
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 文字大小一致14sp
//        view.setMaxLines(7);// 最大行数为7行

//        view.measure(0,0);此时不能让系统自己去测，因为不是写在布局文件里的
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);// 宽不变, 确定值, match_parent
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);// 高度包裹内容, wrap_content;当包裹内容时不确定具体高度,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度

        // 开始测量
        view.measure(widthMeasureSpec, heightMeasureSpec);

        return view.getMeasuredHeight();// 返回测量后的高度
    }

    //找到描述信息TextView的父布局ScrollView
    private ScrollView getScrollView() {
        ViewParent parent = tvDes.getParent();

        while (!(parent instanceof ScrollView)) {
            parent = parent.getParent();
        }

        return (ScrollView) parent;
    }

    // 获取ScrollView, 一层一层往上找,
    // 知道找到ScrollView后才返回;注意:一定要保证父控件或祖宗控件有ScrollView,否则死循环
    @Override
    protected void refreshView(AppInfo data) {
        tvDes.setText(data.des);
        tvAuthor.setText(data.author);

        // 放在消息队列中运行, 解决当只有三行描述时也是7行高度的bug
        tvDes.post(new Runnable() {

            @Override
            public void run() {
                // 默认展示7行的高度
                int shortHeight = getShortHeight();
                mParams = (LinearLayout.LayoutParams) tvDes.getLayoutParams();
                mParams.height = shortHeight;

                tvDes.setLayoutParams(mParams);
            }
        });
    }
}
