package com.maple.mstd.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {

	/** 上一帧播放时间 **/
	private long mLastPlayTime = 0;
	/** 播放当前帧的ID **/
	private int mPlayID = 0;
	/** 动画frame数量 **/
	private int mFrameCount = 0;
	/** 用于储存动画资源图片 **/
	private Bitmap[] mframeBitmap = null;
	/** 是否循环播放 **/
	private boolean mIsLoop = false;
	/** 播放结束 **/
	private boolean mIsend = false;
	/** 动画播放间隙时间 **/
	private int animTime = 200;

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param frameBitmap
	 * @param isloop
	 */
	public Animation(Context context, Bitmap[] frameBitmap, boolean isloop) {
		mFrameCount = frameBitmap.length;
		mframeBitmap = frameBitmap;
		// Log.i("time", String.valueOf(ANIM_TIME));
		mIsLoop = isloop;
	}

	/**
	 * 绘制动画中的其中一帧
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 * @param frameID
	 */
	public void DrawFrame(Canvas Canvas, Paint paint, int x, int y, int frameID) {
		Canvas.drawBitmap(mframeBitmap[frameID], x, y, paint);
	}

	/**
	 * 绘制动画
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void DrawAnimation(Canvas Canvas, Paint paint, int x, int y) {
		// 如果没有播放结束则继续播放
		if (!mIsend) {
			Canvas.drawBitmap(mframeBitmap[mPlayID], x, y, paint);
			long time = System.currentTimeMillis();
			if (time - mLastPlayTime > animTime) {
				mPlayID++;
				mLastPlayTime = time;
				if (mPlayID >= mFrameCount) {
					// 标志动画播放结束
					mIsend = true;
					if (mIsLoop) {
						// 设置循环播放
						mIsend = false;
						mPlayID = 0;
					}
				}
			}
		}
	}

	public int getAnimTime() {
		return animTime;
	}

	public void setAnimTime(int animTime) {
		this.animTime = animTime;
	}

}
