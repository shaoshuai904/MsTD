﻿package com.ms_td.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 获得屏幕相关的辅助类
 * 
 * @author shao
 * 
 */
public class ScreenUtils {
	private ScreenUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 测量高度,调整大小
	 * 
	 * @return
	 */
	public static void measureHeight(ListView listView) {
		// 获取适配器
		ListAdapter adapter = listView.getAdapter();
		if (null == adapter) {
			return;
		}
		int totalHeight = 0;// 总高度
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, listView);
			if (null == item)
				continue;
			// 测量每一个项目的宽度和高度
			item.measure(0, 0);
			// 计算总高度
			totalHeight += item.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (null == params) {
			params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 计算ListView的总高度
		params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 0));
		listView.setLayoutParams(params);
	}

}