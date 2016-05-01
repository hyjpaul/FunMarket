package com.hyj.administrator.funmarket.uiutils;

import com.lidroid.xutils.BitmapUtils;

/**
 * 只需要1个，因为每次new都会分配大约16M内存/8，防止内存溢出
 */
public class BitmapHelper {
    private static BitmapUtils mBitmapUtils = null;

    // 单例, 懒汉模式
    public static BitmapUtils getBitmapUtils() {
        if (mBitmapUtils == null) {
            synchronized (BitmapHelper.class) {
                if (mBitmapUtils == null) {
                    mBitmapUtils = new BitmapUtils(UiUtil.getContext());
                }
            }
        }

        return mBitmapUtils;
    }
}
