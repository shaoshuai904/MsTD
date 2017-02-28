package com.maple.mstd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.maple.mstd.anim.Animation;
import com.maple.mstd.bean.EnemyData;
import com.maple.mstd.utils.BitmapUtils;
import com.maple.mstd.utils.DensityUtils;

/**
 * 怪物
 * 
 * @author shaoshuai
 * 
 */
public class Enemy extends Sprite {
	// 动画数目
	public final static int ANIM_COUNT = 4;

	/** 向下移动动画 **/
	public final static int ANIM_DOWN = 0;
	/** 向左移动动画 **/
	public final static int ANIM_LEFT = 1;
	/** 向右移动动画 **/
	public final static int ANIM_RIGHT = 2;
	/** 向上移动动画 **/
	public final static int ANIM_UP = 3;

	// /** 移动动画 */
	// public enum MoveAnim {
	// Up, Down, Left, Right;
	// }

	// 帧动画人物的宽和高
	int tileWidth = 0;
	int tileHeight = 0;
	/** 记录自己的编号 */
	private int enemyNum = 0;

	/** 记录受到攻击的数值 */
	private int damaged = 0;
	/** 显示受到攻击的数值时间 */
	private int drawDamageTime = 0;

	/** 移动速度 */
	private float moveSpeed;
	/** 记录原始移动速度，因为可能会被减速 */
	private float moveSpeedOrg;
	private int showSpeedSlowTime = 0;

	/** 杀死敌人获得的金钱 */
	private int killedMoney;

	/** 移动方向 */
	public int moveDirection;
	/** 移动路径 */
	Path movePath;
	/** 移动路径下标 */
	int pathIndex = 0;
	/** 帧动画 */
	Animation mEnemyAnim[] = new Animation[ANIM_COUNT];

	/** 怪物血条 */
	HpBar enemyHpBar;
	/** 生命值 */
	private int HP;

	/** 是否显示血条 */
	boolean displayHP = false;
	boolean remove = false;
	/** 是否是流血状态 */
	boolean blood = false;
	int bloodTimes;
	private Context mContext;

	/**
	 * 初始化怪物
	 * 
	 * @param context
	 * @param bitmap
	 *            - 怪物图片
	 * @param x
	 *            - 怪物X坐标
	 * @param y
	 *            - 怪物Y坐标
	 */
	public Enemy(Context context, Bitmap bitmap, float x, float y) {
		super(bitmap, x, y);
		mContext = context;

		HP = 20;
		moveSpeed = 2;
		loadAnimation(context, bitmap);// 加载帧动画
		moveDirection = ANIM_DOWN;

		pathIndex = 0;
		// 设置敌人图片,因为传进来的图片是帧动画合集，所以要根据单个敌人的大小来重新设置
		setmBitmap(BitmapUtils.resizeImage(bitmap, tileWidth, tileHeight));
		// 血条长为怪物宽，高度1dp
		enemyHpBar = new HpBar(tileWidth, DensityUtils.dp2px(mContext, 1), HP);

	}

	/**
	 * 分割图片，加载帧动画
	 * 
	 * @param context
	 * @param testmap
	 */
	private void loadAnimation(Context context, Bitmap testmap) {
		Bitmap[][] bitmap = new Bitmap[ANIM_COUNT][ANIM_COUNT];
		tileWidth = testmap.getWidth() / ANIM_COUNT;
		tileHeight = testmap.getHeight() / ANIM_COUNT;
		int i = 0, x = 0, y = 0;
		for (i = 0; i < ANIM_COUNT; i++) {
			y = 0;
			bitmap[ANIM_DOWN][i] = BitmapUtils.clipBitmap(testmap, x, y, tileWidth, tileHeight);
			y += tileHeight;
			bitmap[ANIM_LEFT][i] = BitmapUtils.clipBitmap(testmap, x, y, tileWidth, tileHeight);
			y += tileHeight;
			bitmap[ANIM_RIGHT][i] = BitmapUtils.clipBitmap(testmap, x, y, tileWidth, tileHeight);
			y += tileHeight;
			bitmap[ANIM_UP][i] = BitmapUtils.clipBitmap(testmap, x, y, tileWidth, tileHeight);
			x += tileWidth;
		}
		mEnemyAnim[ANIM_DOWN] = new Animation(context, bitmap[ANIM_DOWN], true);
		mEnemyAnim[ANIM_LEFT] = new Animation(context, bitmap[ANIM_LEFT], true);
		mEnemyAnim[ANIM_RIGHT] = new Animation(context, bitmap[ANIM_RIGHT], true);
		mEnemyAnim[ANIM_UP] = new Animation(context, bitmap[ANIM_UP], true);
	}

	/**
	 * 加载基本属性数据
	 * 
	 * @param enemyData
	 */
	public void setEnemyData(EnemyData enemyData) {
		HP = enemyData.hp;
		enemyHpBar.setHpOrg(HP);
		moveSpeed = enemyData.moveSpeed;
		moveSpeedOrg = moveSpeed;
		killedMoney = enemyData.killedMoney;
		setAnimationTime(enemyData.animTiem);
	}

	/** 是否减速 */
	public boolean isSpeedSlow() {
		if (moveSpeed != moveSpeedOrg) {
			return true;
		}
		return false;
	}

	/** 恢复移动速度 */
	public void recoverSpeed() {
		moveSpeed = moveSpeedOrg;
	}

	/**
	 * 减速时间是否到
	 * 
	 * @return
	 */
	public boolean isSlowTimeEnd() {
		if (showSpeedSlowTime >= 30) {
			showSpeedSlowTime = 0;
			return true;
		} else {
			showSpeedSlowTime++;
			return false;
		}
	}

	/** 流血状态是否结束 */
	public boolean isBloodTimeEnd() {
		if (bloodTimes > 50) {// 流血 -50Hp
			bloodTimes = 0;
			blood = false;
			return true;
		} else {
			bloodTimes++;
			if (HP > 0) {
				HP = HP - 1;
			} else if (HP == 0) {
				blood = false;
				return true;
			}
			return false;
		}
	}

	/** 根据帧动画绘制精灵图片 */
	public void drawmSpriteBitmap(Canvas canvas) {
		mEnemyAnim[moveDirection].DrawAnimation(canvas, null, (int) getX(), (int) getY());
		if (isDisplayHP()) {
			float drawX = 0;
			float drawY = 0;
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setTextAlign(Paint.Align.CENTER);// 居中
			paint.setTextSize(30);
			if (damaged > 0) {
				// 减血数字 在怪物头顶 3.5dp
				drawX = getX() + tileWidth / 2;
				drawY = getY() - DensityUtils.dp2px(mContext, 3.8f);
				canvas.drawText("-" + damaged, drawX, drawY, paint);
				if (drawDamageTime >= 10) {
					damaged = 0;
					drawDamageTime = 0;
				} else {
					drawDamageTime++;
				}
			}
			// 血条在怪物头顶2dp,水平居中
			drawX = getX() + tileWidth / 2 - enemyHpBar.getHpBarLength() / 2;
			drawY = getY() - DensityUtils.dp2px(mContext, 2);
			enemyHpBar.drawHpBar(canvas, drawX, drawY, HP);
		}
	}

	// /**
	// * 根据方向移动，现在已经不用了，但是还是先留着吧
	// */
	// public void move() {
	// switch (moveDirection) {
	// case ANIM_LEFT:
	// setCircleX(circleX - moveSpeed);
	// break;
	// case ANIM_UP:
	// setCircleY(circleY - moveSpeed);
	// break;
	// case ANIM_RIGHT:
	// setCircleX(circleX + moveSpeed);
	// break;
	// case ANIM_DOWN:
	// setCircleY(circleY + moveSpeed);
	// break;
	// }
	// }

	/** 开始减速 */
	public void StartSlow() {
		showSpeedSlowTime = 0;
	}

	/** 移动速度自增 */
	public void increaseMoveSpeed() {
		moveSpeed = moveSpeed + 1;
	}

	/** 设置帧动画间隔 */
	public void setAnimationTime(int time) {
		for (int i = 0; i < ANIM_COUNT; i++) {
			mEnemyAnim[i].setAnimTime(time);
		}
	}

	/** 根据路径移动，这里是重点 */
	public void moveByPath() {
		// 坐标节点数组
		final float[] mCoordinatesX;
		final float[] mCoordinatesY;

		mCoordinatesX = movePath.getCoordinatesX();
		mCoordinatesY = movePath.getCoordinatesY();
		// 获得移动方向
		if (mCoordinatesX[pathIndex] > X) {
			moveDirection = ANIM_RIGHT;
		} else if (mCoordinatesX[pathIndex] < X) {
			moveDirection = ANIM_LEFT;
		} else if (mCoordinatesY[pathIndex] > Y) {
			moveDirection = ANIM_DOWN;
		} else if (mCoordinatesY[pathIndex] < Y) {
			moveDirection = ANIM_UP;
		}

		switch (moveDirection) {
		case ANIM_LEFT:
			setX(X - moveSpeed);
			if (X < mCoordinatesX[pathIndex]) {
				X = mCoordinatesX[pathIndex];
			}
			break;
		case ANIM_UP:
			setY(Y - moveSpeed);
			if (Y <= mCoordinatesY[pathIndex]) {
				Y = mCoordinatesY[pathIndex];
			}
			break;
		case ANIM_RIGHT:
			setX(X + moveSpeed);
			if (X > mCoordinatesX[pathIndex]) {
				X = mCoordinatesX[pathIndex];
			}
			break;

		case ANIM_DOWN:
			setY(Y + moveSpeed);
			if (Y >= mCoordinatesY[pathIndex]) {
				Y = mCoordinatesY[pathIndex];
			}
			break;
		}
		// 到达路径节点，则换下一个路径，即节点下标++
		// pathIndex++;
		if (X == mCoordinatesX[pathIndex] && Y == mCoordinatesY[pathIndex]) {
			pathIndex++;
		}

		if (pathIndex == movePath.getSize()) {
			pathIndex = movePath.getSize() - 1;
			remove = true;
		}
	}

	// ------------------------------------------------------------------------
	/** 是否是流血状态 */
	public boolean isBlood() {
		return blood;
	}

	/** 设置流血状态 */
	public void setBlood(boolean blood) {
		this.blood = blood;
	}

	public boolean isRemove() {
		return remove;
	}

	/** 是否显示血条 */
	public boolean isDisplayHP() {
		return displayHP;
	}

	/** 设置是否显示血条 */
	public void setDisplayHP(boolean displayHP) {
		this.displayHP = displayHP;
	}

	public int getKilledMoney() {
		return killedMoney;
	}

	public void setKilledMoney(int killedMoney) {
		this.killedMoney = killedMoney;
	}

	public int getAnimationTime() {
		return mEnemyAnim[0].getAnimTime();
	}

	public int getEnemyNum() {
		return enemyNum;
	}

	public void setEnemyNum(int enemyNum) {
		this.enemyNum = enemyNum;
	}

	public int getHP() {
		return HP;
	}

	// /** 设置血量 */
	// public void setHP(int hp) {
	// HP = hp;
	// enemyHpBar.setHpOrg(hp);
	// }

	/** 血量减少 */
	public void reduceHP(int hp) {
		damaged = hp;
		HP = HP - hp;
		drawDamageTime = 0;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float speed) {
		moveSpeed = speed;
	}

	/** 设置移动方向 */
	public void setDirection(int d) {
		moveDirection = d;
	}

	public float getMoveSpeedOrg() {
		return moveSpeedOrg;
	}

	public void setMoveSpeedOrg(float moveSpeedOrg) {
		this.moveSpeedOrg = moveSpeedOrg;
	}

	public Path getMovePath() {
		return movePath;
	}

	/** 设置移动路径 */
	public void setMovePath(Path movePath) {
		// 路徑下标重新置0
		pathIndex = 0;
		this.movePath = movePath;
	}

	/** 获得单个敌人的宽度 */
	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/** 获得单个敌人的高度 */
	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
}
