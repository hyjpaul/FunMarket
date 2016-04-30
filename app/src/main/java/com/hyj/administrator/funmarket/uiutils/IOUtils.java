package com.hyj.administrator.funmarket.uiutils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 别人写好的
 */
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
