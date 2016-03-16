package com.ms_td.game;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;

/**
 * 炮台
 * 
 * @author shaoshuai
 * 
 */
public class Tower extends Sprite {

	int row; // 炮塔所在行和列
	int col;

	/** 最大攻击力 */
	public final int MaxDamage = 15;
	/** 炮塔攻击力，应该是子弹的攻击力 */
	int damage;

	/** 最大攻击范围 */
	public final float MAXATTACKR = 450f;
	/** 攻击范围 */
	float attackR = 300f;
	/** 是否绘制攻击范围 */
	boolean drawAttackR = false;

	private int mulAttckNum = 3;
	/** 攻击间隔1000毫秒 */
	private int attackTimes = 1000;
	/** 用于记录当前攻击间隔 */
	private int attackTimesOrg = 1000;
	/** 最小攻击间隔 */
	public final int MinAttackTimes = 400;
	/** 标记是否是最小攻击间隔 */
	private boolean minAttackTimes = false;
	/** 最小攻击间隔持续时间计数器 */
	private int minAttackSpeedTime = 0;

	protected int timesCount = 0; // 攻击间隔计数器

	/** 子弹图片 */
	Bitmap bulletBitmap = null;

	/** 炮塔类型，用于判断该炮塔的特效 */
	private int towerType = 0;

	public final static int NORMAL = 0;
	/** 减速 */
	public final static int SLOW = 2;
	/** 【炮台】多重攻击 */
	public final static int MulAttack = 3;
	/** 【火箭炮】多重伤害 */
	public final static int MulDamage = 1;

	/** 子弹集合 */
	protected final ArrayList<BulletSprite> bullets = new ArrayList<BulletSprite>();

	public Tower(Bitmap bitmap, float x, float y) {
		super(bitmap, x, y);
		circleX = x + bitmap.getWidth() / 2;
		circleY = y + bitmap.getHeight() / 2;
		timesCount = 0;
		damage = 5;
	}

	public void addBullet(float enemyX, float enemyY, int enemyNum) {
		double degress;
		BulletSprite tBitmap = new BulletSprite(bulletBitmap, circleX - 10, circleY - 20);
		tBitmap.setEnemyNum(enemyNum);
		tBitmap.setEnemyX(enemyX);
		tBitmap.setEnemyY(enemyY);
		tBitmap.setDamage(damage);
		degress = tBitmap.getRotation(enemyX, enemyY);
		tBitmap.setRotation(degress);
		bullets.add(tBitmap);
	}

	public void addBullet(Bitmap mBitmap, float enemyX, float enemyY, int enemyNum) {
		BulletSprite tBitmap = new BulletSprite(mBitmap, circleX, circleY);
		tBitmap.setEnemyNum(enemyNum);
		tBitmap.setEnemyX(enemyX);
		tBitmap.setEnemyY(enemyY);
		tBitmap.setDamage(damage);
		bullets.add(tBitmap);

	}

	/** 判断子弹是否超出攻击范围 */
	public boolean isBulletOutOfAttackR(int index) {
		float x1, x2;
		float y1, y2;
		double r1;
		double r2;
		x1 = circleX;
		y1 = circleY;
		r1 = attackR;

		x2 = bullets.get(index).getCircleX();
		y2 = bullets.get(index).getCircleY();
		r2 = bullets.get(index).getCircleR();
		double tem = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		if (tem >= (r1 - r2)) {
			return true;
		}
		return false;
	}

	/** 判断敌人是否进入攻击范围 */
	public boolean isIntoAttackRange(Sprite enemy) {
		float x1, x2;
		float y1, y2;
		// double r1;
		float r2;
		x1 = enemy.getCircleX();
		y1 = enemy.getCircleY();
		// r1 = enemy.getCircleR();
		x2 = circleX;
		y2 = circleY;
		r2 = attackR;
		double tem = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		// Log.i("tmp", String.valueOf(r1 + r2));
		if (tem <= r2) {
			return true;
		}
		return false;
	}

	/** 时间间隔增加 */
	public void IncreaseTimes() {
		timesCount = timesCount + 100;
		// Log.i("IncreaseAttackTimes", String.valueOf(AttackTimes));
	}

	/** 获取随机伤害倍数 */
	public int getMulDamage() {
		int tmp = new Random().nextInt(100);
		if (tmp < 3) {// 百分之3的几率5倍伤害
			return 5;
		} else if (tmp < 10) {
			return 4;
		} else if (tmp < 20) {
			return 3;
		} else if (tmp < 40) {
			return 2;
		} else {
			return 1;
		}
	}

	/** 是否是最小攻击速度 */
	public boolean isMinAttackSpeed() {
		if (towerType == NORMAL) {
			Random random = new Random();
			if (random.nextInt(100) < 5 && minAttackTimes == false) {
				minAttackTimes = true;
				return true;
			}
		}
		return false;
	}

	public boolean isminAttackSpeedTimeEnd() {
		if (minAttackSpeedTime >= 5) {
			minAttackSpeedTime = 0;
			minAttackTimes = false;
			return true;
		} else {
			minAttackSpeedTime++;
			return false;
		}
	}

	/** 获取子弹总数 */
	public int getBulletsSize() {
		return bullets.size();
	}

	/** 获取指定序列的子弹 */
	public BulletSprite getBullet(int index) {
		return bullets.get(index);
	}

	/** 移除指定序列的子弹 */
	public void removeBullet(int index) {
		bullets.remove(index);
	}

	/** 是否双重伤害 */
	public boolean isDoubleDamage() {
		return towerType == MulDamage;
	}

	/** 炮台类型是否为减速魔法塔 */
	public boolean isSlow() {
		return towerType == SLOW;
	}

	// ----------------------get-set--------------------------------------
	/** 获取炮台的攻击范围 */
	public float getAttackR() {
		return attackR;
	}

	/** 设置炮台的攻击范围 */
	public void setAttackR(float attackR) {
		this.attackR = attackR;
		// 多重箭的时候根据攻击范围增加箭的个数
		if (towerType == MulAttack) {
			if (attackR >= 145f) {
				mulAttckNum = 6;
			} else if (attackR >= 130f) {
				mulAttckNum = 5;
			} else if (attackR >= 115f) {
				mulAttckNum = 4;
			}
		}
	}

	public int getMulAttackNum() {
		return mulAttckNum;
	}

	/** 是否绘制攻击范围 */
	public boolean isDrawAttackR() {
		return drawAttackR;
	}

	/** 设置是否绘制攻击范围 */
	public void setDrawAttackR(boolean drawAttackR) {
		this.drawAttackR = drawAttackR;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	/** 获取炮台类型 */
	public int getTowerType() {
		return towerType;
	}

	/** 设置炮台类型 */
	public void setTowerType(int towerType) {
		this.towerType = towerType;
	}

	/** 攻击间隔计数器 */
	public int getTimesCount() {
		return timesCount;
	}

	public void setTimesCount(int time) {
		timesCount = time;
	}

	/** 是否是最小攻击间隔 */
	public boolean isMinAttackTimes() {
		return minAttackTimes;
	}

	/** 获取攻击间隔 */
	public int getAttackTimes() {
		return attackTimes;
	}

	/** 设置攻击间隔 */
	public void setAttackTimes(int attackTimes) {
		this.attackTimes = attackTimes;
	}

	public int getAttackTimesOrg() {
		return attackTimesOrg;
	}

	public void setAttackTimesOrg(int attackTimesOrg) {
		this.attackTimesOrg = attackTimesOrg;
	}

	/** 获取子弹攻击力 */
	public int getDamage() {
		return damage;
	}

	/** 设置子弹攻击力 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Bitmap getBulletBitmap() {
		return bulletBitmap;
	}

	public void setBulletBitmap(Bitmap bulletBitmap) {
		this.bulletBitmap = bulletBitmap;
	}

}
