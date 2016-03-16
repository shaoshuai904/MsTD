package com.ms_td.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 血条
 * 
 * @author shaoshuai
 * 
 */
public class HpBar {

	private int hpBarLength;// 血条长度
	private int hpBarHeight;// 血条高度

	private int hpOrg;// 总血量

	public HpBar(int vWidth, int vHeigth, int hp) {
		this.hpBarLength = vWidth;
		this.hpBarHeight = vHeigth;
		this.hpOrg = hp;
	}

	/**
	 * 更新血条
	 * 
	 * @param canvas
	 * @param x
	 *            - 绘制位置X
	 * @param y
	 *            - 绘制位置Y
	 * @param hp
	 *            - 最新血量
	 */
	public void drawHpBar(Canvas canvas, float x, float y, int hp) {
		if (hp > 0) {
			float scale = ((float) hp / hpOrg);// 血量百分比
			Paint mPaint = new Paint();
			mPaint.setColor(Color.RED);
			canvas.drawRect(x, y, x + hpBarLength * scale, y + hpBarHeight, mPaint);// 剩余血量
			mPaint.setColor(Color.YELLOW);
			mPaint.setAlpha(50);
			canvas.drawRect(x + hpBarLength * scale, y, x + hpBarLength, y + hpBarHeight, mPaint);// 已空血量
		}
	}

	// -------------------get-set-------------------------------
	/** 获取总血量 */
	public int getHpOrg() {
		return hpOrg;
	}

	/** 设置总血量 */
	public void setHpOrg(int hpOrg) {
		this.hpOrg = hpOrg;
	}

	/** 获取血条长度 */
	public int getHpBarLength() {
		return hpBarLength;
	}

	/** 设置血条长度 */
	public void setHpBarLength(int hpBarLength) {
		this.hpBarLength = hpBarLength;
	}

	/** 获取血条高度 */
	public int getHpBarHeight() {
		return hpBarHeight;
	}

	/** 设置血条高度 */
	public void setHpBarHeight(int hpBarHeight) {
		this.hpBarHeight = hpBarHeight;
	}

}
