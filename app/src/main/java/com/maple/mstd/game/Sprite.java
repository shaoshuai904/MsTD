package com.maple.mstd.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 基础图片
 * 
 * @author shaoshuai
 * 
 */
public class Sprite {
	// 绘制位置
	float X;
	float Y;
	// 圆心坐标
	float circleX;
	float circleY;

	/** 圆心半径 */
	double circleR;
	/** 精灵图片 */
	Bitmap mBitmap;

	public Sprite(Bitmap bitmap, float x, float y) {
		mBitmap = bitmap;
		X = x;
		Y = y;
		circleX = mBitmap.getWidth() / 2 + X;
		circleY = mBitmap.getHeight() / 2 + Y;
		circleR = Math.sqrt(Math.pow(mBitmap.getWidth() / 2, 2) + Math.pow(mBitmap.getHeight() / 2, 2));
	}

	/** 绘制精灵图片 */
	public void drawmSpriteBitmap(Canvas canvas) {
		canvas.drawBitmap(mBitmap, X, Y, null);
	}

	public void BitmapRecycle() {
		mBitmap.recycle();
	}

	/** 得到精灵图片和敌人的夹角， 以便于旋转图片 */
	public double getRotation(float x2, float y2) {
		return Math.toDegrees(Math.atan2((y2 - Y), (x2 - X)));
	}

	/** 旋转图片 */
	public void setRotation(double degress) {
		Matrix matrix = new Matrix();
		matrix.postRotate((float) (degress) % 360);
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
	}

	/** 重置图片 并计算圆心坐标和半径 */
	public void resetBitmap(Bitmap bitmap, float x, float y) {
		setmBitmap(bitmap);
		X = x;
		Y = y;
	}

	/** 设置位置 */
	public void setSpriteLocation(float x, float y) {
		setX(x);
		setY(y);
	}

	// -------------------------get-set-----------------------------
	/** 获得精灵图片 */
	public Bitmap getmBitmap() {
		return mBitmap;
	}

	/** 设置精灵图片, 重新计算圆心坐标和半径 */
	public void setmBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;

		circleX = mBitmap.getWidth() / 2 + X;
		circleY = mBitmap.getHeight() / 2 + Y;
		circleR = Math.sqrt(Math.pow(mBitmap.getWidth() / 2, 2) + Math.pow(mBitmap.getHeight() / 2, 2));
	}

	/** 获得图片宽 */
	public int getImageWidth() {
		return mBitmap.getWidth();
	}

	/** 获得图片高 */
	public int getImageHeight() {
		return mBitmap.getHeight();

	}

	/** 获得X坐标 */
	public float getX() {
		return X;
	}

	/** 设置X坐标， 并且根据X坐标设置圆心X坐标 */
	public void setX(float x) {
		this.X = x;
		circleX = mBitmap.getWidth() / 2 + X;
	}

	/** 获得Y坐标 */
	public float getY() {
		return Y;
	}

	/** 设置Y坐标， 并且根据Y坐标设置圆心Y坐标 */
	public void setY(float y) {
		this.Y = y;
		circleY = mBitmap.getHeight() / 2 + Y;
	}

	/** 获得圆心X坐标 */
	public float getCircleX() {
		return circleX;
	}

	/** 设置圆心X坐标 */
	public void setCircleX(float circleX) {
		this.circleX = circleX;
		X = circleX - mBitmap.getWidth() / 2;
	}

	/** 获得圆心Y坐标 */
	public float getCircleY() {
		return circleY;
	}

	/** 设置圆心Y坐标 */
	public void setCircleY(float circleY) {
		this.circleY = circleY;
		Y = circleY - mBitmap.getHeight() / 2;
	}

	/** 获得半径 */
	public double getCircleR() {
		return circleR;
	}

	/** 设置半径 */
	public void setCircleR(double circleR) {
		this.circleR = circleR;
	}
}
