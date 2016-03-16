package com.ms_td.game;

import android.graphics.Bitmap;

/**
 * 子弹
 * 
 * @author shaoshuai
 * 
 */
public class BulletSprite extends Sprite {

	/** 目标点X */
	float enemyX;
	/** 目标点Y */
	float enemyY;

	/** 火力值 */
	int damage = 5;
	/** 敌人编号 */
	int enemyNum = -1;

	public BulletSprite(Bitmap bitmap, float x, float y) {
		super(bitmap, x, y);
		// TODO Auto-generated constructor stub
	}

	/** 检测是否碰撞敌人 */
	public boolean isCollidedEnemy(Sprite enemy) {
		float x1, x2;
		float y1, y2;
		double r1;
		double r2;
		x1 = enemy.getCircleX();
		y1 = enemy.getCircleY();
		r1 = enemy.getCircleR();
		x2 = circleX;
		y2 = circleY;
		r2 = circleR;
		double tem = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		if (tem <= (r1 + r2) / 2) {
			return true;
		}
		return false;
	}

	/**
	 * 移动子弹，这里根据子弹圆心坐标与敌人的坐标距离3分之一来移动
	 */
	public void move() {
		if (circleX < enemyX) {
			circleX = circleX + (enemyX - circleX) / 3;
		} else {
			circleX = circleX - (circleX - enemyX) / 3;
		}
		if (circleY < enemyY) {
			circleY = circleY + (enemyY - circleY) / 3;
		} else {
			circleY = circleY - (circleY - enemyY) / 3;
		}
		X = circleX - mBitmap.getWidth() / 2;
		Y = circleY - mBitmap.getHeight() / 2;
	}

	// ------------get-set------------------------------
	/** 获取目标点X */
	public float getEnemyX() {
		return enemyX;
	}

	/** 设置目标点X */
	public void setEnemyX(float enemyX) {
		this.enemyX = enemyX;
	}

	/** 获取目标点Y */
	public float getEnemyY() {
		return enemyY;
	}

	/** 设置目标点Y */
	public void setEnemyY(float enemyY) {
		this.enemyY = enemyY;
	}

	/** 获取火力值 */
	public int getDamage() {
		return damage;
	}

	/** 设置火力值 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/** 获取敌人编号 */
	public int getEnemyNum() {
		return enemyNum;
	}

	/** 设置敌人编号 */
	public void setEnemyNum(int enemyNum) {
		this.enemyNum = enemyNum;
	}
}
