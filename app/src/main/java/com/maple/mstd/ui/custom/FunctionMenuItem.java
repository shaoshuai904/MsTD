package com.maple.mstd.ui.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * 菜单项Item
 * 
 * @author shaoshuai
 * 
 */
public class FunctionMenuItem {
	public float vWidth; // 视图宽高
	public float vHeight;

	public float drawX = 0; // 绘制坐标
	public float drawY = 0;

	Bitmap mBitmap = null;
	String txtDes = null;

	int txtColor = Color.GREEN; // 字体颜色
	int txtSize = 30;

	public FunctionMenuItem(float width, float height, Bitmap bmp, String txt) {
		this.vWidth = width;
		this.vHeight = height;

		this.mBitmap = bmp;
		this.txtDes = txt;
	}

	/** 设置绘制坐标 */
	public void setDrawXY(float menuX, float menuY) {
		drawX = menuX;
		drawY = menuY;
	}

	/** 绘制菜单条目 */
	public void drawMenuItem(Canvas canvas) {
		float x = 0;
		float y = 0;
		// 只有图片
		if (mBitmap != null && txtDes == null) {
			Paint paint = new Paint();
			paint.setAlpha(500);

			x = drawX + vWidth / 2 - mBitmap.getWidth() / 2;// 水平居中
			y = drawY + vHeight / 2 - mBitmap.getHeight() / 2;// 竖直居中
			canvas.drawBitmap(mBitmap, x, y, null);
		}
		// 只有文字
		if (txtDes != null && mBitmap == null) {
			Paint paint = new Paint();
			paint.setAlpha(100);
			paint.setTextAlign(Paint.Align.CENTER);// 居中
			paint.setTextSize(txtSize);
			paint.setColor(txtColor);
			x = drawX + vWidth / 2;// 视图中间点
			y = (drawY * 2 + vHeight - paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2;// 竖直居中
			canvas.drawText(txtDes, x, y, paint);
		}
		// 图文混合
		if (mBitmap != null && txtDes != null) {
			Paint paint = new Paint();
			// 图片
			paint.setAlpha(500);
			x = drawX;//
			y = drawY + vHeight / 2 - mBitmap.getHeight() / 2;// 竖直居中
			canvas.drawBitmap(mBitmap, x, y, null);
			// 文本
			paint.setAlpha(100);
			paint.setTextAlign(Paint.Align.CENTER);// 居中
			paint.setTextSize(txtSize);
			paint.setColor(txtColor);
			x = drawX + mBitmap.getWidth() + (vWidth - mBitmap.getWidth()) / 2;// 减去图片的视图中间点
			y = (drawY * 2 + vHeight - paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2;// 竖直居中
			canvas.drawText(txtDes, x, y, paint);
		}
	}

	/** 绘制选中条目的边框 */
	public void drawItemSelected(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAlpha(100);
		paint.setStrokeWidth(3.0f); // 线宽
		paint.setStyle(Style.STROKE); // 空心效果
		// 更新位置
		RectF mRect = new RectF(drawX, drawY, drawX + vWidth, drawY + vHeight);
		canvas.drawRect(mRect, paint);

	}

	/** 是否点击某项 */
	public boolean itemClicked(float x, float y) {
		if (drawX <= x && x <= drawX + vWidth && drawY <= y && y <= drawY + vHeight) {
			return true;
		}
		return false;
	}
}
