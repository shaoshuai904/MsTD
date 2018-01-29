package com.maple.mstd.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.maple.mstd.EnemyFactory;
import com.maple.mstd.LeMapDes;
import com.maple.mstd.LeMapPath;
import com.maple.mstd.R;
import com.maple.mstd.bean.EnemyData;
import com.maple.mstd.game.Enemy;
import com.maple.mstd.game.Path;
import com.maple.mstd.game.Tower;
import com.maple.mstd.utils.BitmapUtils;
import com.maple.mstd.utils.ScreenUtils;
import com.maple.mstd.utils.T;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 游戏视图
 *
 * @author shaoshuai
 */
public class GameView extends View {
    Context mContext;
    Paint mPaint;
    Lock lock;

    private int vWidth = 480;// 本视图的宽
    private int vHeight = 800;// 本视图的高

    private float bgWidth = 64;// 单元格的宽
    private float bgHeight = 64;// 单元格的高

    private boolean running;
    /** 是否暂停 */
    private boolean pause = false;
    /** 是否画表格 */
    private boolean isDrawMap = false;
    /** 是否画炮台 */
    private boolean isDrawTower = false;
    /** 是否画爆炸 */
    private boolean isDrawBomb = false;

    /** 背景图片 */
    Bitmap backGround;
    /** 炮台图片 */
    Bitmap[] tower = new Bitmap[4];
    /** 爆炸图片 */
    Bitmap bomb;
    /** 眩晕图片 */
    Bitmap vertigo;
    /** 减速图片 */
    Bitmap speedSlow;
    /** 流血图片 */
    Bitmap blood;

    /** 用来记录临时炮塔的坐标 */
    float towerX, towerY;
    /** 爆炸效果的坐标 */
    float bombX, bombY;
    /** 敌人起始坐标 */
    private float enemyX, enemyY;

    /** 当前位置 */
    // private WeiZhi curWz;
    /** 当前行 */
    private int curRow;
    /** 当前列 */
    private int curCol;
    /** 逃跑数量 */
    private int runAwayNum;
    /** 杀死数量 */
    private int killNum;
    /** 金钱 */
    private int momey;

    /** 用于记录当前选择的炮塔 */
    private int selectedTower;
    /** 用于记录炮塔选择菜单的选择项 */
    private int itemSelected;

    /** 游戏关数 */
    private int levelNum;
    /** 第几波敌人 */
    private int enemyLevel;
    /** 当前敌人数量 */
    private int curEnemyCount;
    /** 记录敌人编号,没一个敌人分配一个编号 */
    private int enemyNum = 1;

    /** 敌人移动速度 */
    private float enemyMoveSpeed;
    /** 敌人血量 */
    private int HP;
    /** 敌人名称 */
    String enemyName = null;
    /** 地图数组 */
    private int map[][];

    /** 敌人列表 */
    ArrayList<Enemy> enemys = new ArrayList<Enemy>();
    /** 炮塔列表 */
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    /** 炮塔选择菜单 */
    private FunctionMenu mGameMenu; //
    /** 炮塔升级菜单 */
    private FunctionMenu towerMenu;
    private EnemyFactory mConst;
    /** 菜单拖拽 */
    private boolean dragging;

    /** 长按事件元素 */
    private int mLastMotionX, mLastMotionY;
    /** 是否移动了 */
    private boolean isMoved;
    /** 是否释放了 */
    private boolean isReleased;
    // /** 计数器，防止多次点击导致最后一次形成longpress的时间变短 */
    // private int mCounter;

    /** 长按的runnable */
    private Runnable mLongPressRunnable;
    /** 移动的阈值 */
    private static final int TOUCH_SLOP = 20 * 2;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mConst = new EnemyFactory();// 常量
        lock = new ReentrantLock();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(24);

        levelNum = 1;// 第一关

        loadImage();
        running = true;
        loadDataByLevel(levelNum);

        new EnemyThread().start();
        new TowerThread().start();

        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                // mCounter--;
                // 计数器大于0，说明当前执行的Runnable不是最后一次down产生的。
                if (isReleased || isMoved)
                    // if (mCounter > 0 || isReleased || isMoved)
                    return;
                // 自定义长按事件处理函数
                LongClick();
            }
        };
    }

    int[] torerBgArr = {R.drawable.tower1, R.drawable.tower2, R.drawable.tower3, R.drawable.tower9};
    String[] torerDesArr = {"箭塔", "炮台", "减速塔", "火箭井"};
    String[] strs = {"攻击强度", "攻击范围", "攻击速度", "取消菜单"};

    private void loadImage() {
        vWidth = ScreenUtils.getScreenWidth(mContext);
        vHeight = ScreenUtils.getScreenHeight(mContext);
        bgWidth = ((float) vWidth) / LeMapDes.cols;
        bgHeight = ((float) vHeight) / LeMapDes.rows;

        // 特效
        bomb = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.bomb, bgWidth / 2, bgHeight / 2);// 爆炸
        vertigo = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.xuanyun, bgWidth / 2, bgHeight / 2);// 眩晕
        speedSlow = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.jiansu, bgWidth / 2, bgHeight / 2);// 减速
        blood = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.blood, bgWidth / 2, bgHeight / 2);// 流血

        // 武器菜单
        ArrayList<FunctionMenuItem> mItems = new ArrayList<>();
        for (int i = 0; i < torerBgArr.length; i++) {
            tower[i] = BitmapUtils.readAndResizeBitmap(mContext, torerBgArr[i], bgWidth, bgHeight);
            mItems.add(new FunctionMenuItem(bgWidth * 2.5f, bgHeight, tower[i], torerDesArr[i]));
        }
        mGameMenu = new FunctionMenu(bgWidth * 3, bgHeight * 5, mItems);
        // 升级菜单
        Bitmap upgrade = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.upgrade, bgWidth, bgHeight);// 升级
        ArrayList<FunctionMenuItem> mUpItems = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            mUpItems.add(new FunctionMenuItem(bgWidth * 3f, bgHeight, upgrade, strs[i]));
        }
        towerMenu = new FunctionMenu(bgWidth * 4, bgHeight * 5, mUpItems);
        towerMenu.setVisibility(false);

    }

    public void LongClick() {
        // 显示菜单
        towerMenu.setVisibility(true);
        // 根据当前选中的炮塔设置菜单的位置
        for (int i = 0; i < towers.size(); i++) {
            if (curRow == towers.get(i).getRow() && curCol == towers.get(i).getCol()) {
                // 设置菜单的坐标即菜单的位置
                float draX = towers.get(i).getX() - towerMenu.vWidth / 2;
                float draY = towers.get(i).getY() - towerMenu.vHeight / 2;
                towerMenu.setMenuLocation(draX, draY);
                break;
            }
        }
    }

    /** 根据游戏当前关数加载数据 */
    private void loadDataByLevel(int levelNum) {
        backGround = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.map1 + (levelNum - 1), vWidth, vHeight);
        map = LeMapDes.loadMapLayout(levelNum);

        killNum = 0;
        curEnemyCount = 0;
        enemyLevel = 0;
        runAwayNum = 0;
        selectedTower = 0;
        itemSelected = 0;
        // curWz = new WeiZhi(0, 0);
        curCol = 0;
        curRow = 0;
        enemyNum = 1;
        mGameMenu.setSelectedItem(selectedTower);

        switch (levelNum) {
            case 1:
                momey = 40;
                enemyX = 5.5f * bgWidth;
                enemyY = 0;
                mGameMenu.setMenuLocation(0, 0);
                break;
            case 2:
                momey = 25;
                enemyX = 9.5f * bgWidth;
                enemyY = 0;
                mGameMenu.setMenuLocation(0, 0);
                break;
            case 3:
                momey = 25;
                enemyX = 0.8f * bgWidth;
                enemyY = 0;
                mGameMenu.setMenuLocation(bgWidth * 4, 0);
                break;
            case 4:
                momey = 25;
                enemyX = 3.5f * bgWidth;
                enemyY = 0;
                mGameMenu.setMenuLocation(0, 0);
            default:
                break;
        }
    }

    /** 重新开始 */
    public void resetGame() {
        loadDataByLevel(levelNum);
        towers.removeAll(towers);
        enemys.removeAll(enemys);
        pause = false;
    }

    /** 下一关 */
    public void nextGame() {
        levelNum++;
        if (levelNum > 4) {
            levelNum = 1;
        }
        resetGame();
    }

    /** 进入指定关卡 */
    public void setGameLevel(int levelNum) {
        this.levelNum = levelNum;
        if (levelNum < 1) {
            levelNum = 1;
        }
        if (levelNum > 4) {
            levelNum = 4;
        }
        resetGame();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backGround, 0, 0, null);
        lock.lock();
        drawTower(canvas);// 画炮台
        drawEnemy(canvas);// 画怪物
        lock.unlock();
        drawMessage(canvas);// 画消息

        if (isDrawMap) {// 是否画表格
            drawMap(canvas);
        }
        if (isDrawTower) {// 画选中炮台
            Paint paint = new Paint();
            paint.setAlpha(100);
            canvas.drawBitmap(tower[itemSelected], towerX, towerY, paint);
        }
        if (isDrawBomb) {// 是否画爆炸
            canvas.drawBitmap(bomb, bombX, bombY, null);
            isDrawBomb = false;
        }
        if (mGameMenu != null) {// 武器菜单
            mGameMenu.drawGameMenu(canvas);
        }
        if (towerMenu != null) {// 升级菜单
            towerMenu.drawGameMenu(canvas);
        }
        invalidate();
    }

    /** 画炮台 */
    private void drawTower(Canvas mCanvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(50);
        for (int i = 0; i < towers.size(); i++) {
            // 绘制炮塔
            towers.get(i).drawmSpriteBitmap(mCanvas);
            // if (towers.get(i).isDrawAttackR()) {
            // // 点击炮塔的时候绘制炮塔的攻击范围
            // mCanvas.drawCircle(towers.get(i).getCircleX(), towers.get(i)
            // .getCircleY(), towers.get(i).getAttackR(), paint);
            // }
            // 绘制塔的子弹
            for (int j = 0; j < towers.get(i).getBulletsSize(); j++) {
                // towers.get(i).getBullet(j).drawBall(mCanvas);
                (towers.get(i).getBullet(j)).drawmSpriteBitmap(mCanvas);
            }
        }
    }

    /** 画怪物 */
    private void drawEnemy(Canvas canvas) {
        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).drawmSpriteBitmap(canvas);
            if (enemys.get(i).getMoveSpeed() == 0) {// 眩晕
                canvas.drawBitmap(vertigo, enemys.get(i).getX() + 5, enemys.get(i).getY() - vertigo.getHeight(), null);
            }
            if (enemys.get(i).isSpeedSlow()) {// 减速
                canvas.drawBitmap(speedSlow, enemys.get(i).getX() + 2, enemys.get(i).getY() - speedSlow.getHeight(),
                        null);
            }
            if (enemys.get(i).isBlood()) {// 流血
                canvas.drawBitmap(blood, enemys.get(i).getX() + 5, enemys.get(i).getY() - blood.getHeight(), null);
            }
        }
    }

    /** 绘制消息 */
    protected void drawMessage(Canvas canvas) {
        float drawX = 20;
        float drawY = vHeight - 350;
        canvas.drawText("第 " + (enemyLevel + 1) + " 波敌人", drawX, drawY, mPaint);
        if (enemyName != null) {
            canvas.drawText(enemyName, drawX, drawY + 50, mPaint);
        }
        canvas.drawText("生命值: " + HP, drawX, drawY + 100, mPaint);
        canvas.drawText("移动速度: " + enemyMoveSpeed, drawX, drawY + 150, mPaint);
        canvas.drawText("逃脱: " + runAwayNum, drawX, drawY + 200, mPaint);
        canvas.drawText("金钱: " + momey, drawX, drawY + 250, mPaint);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(50);
        for (int i = 0; i < towers.size(); i++) {
            Tower tower = towers.get(i);
            if (tower.isDrawAttackR()) {
                selTower = i;
                // 点击炮塔的时候绘制炮塔的攻击范围
                canvas.drawCircle(tower.getCircleX(), tower.getCircleY(), tower.getAttackR(), paint);
            }
        }
        drawX = vWidth - 300;
        drawY = vHeight - 300;
        if (towers != null && towers.size() > selTower) {
            Tower tower = towers.get(selTower);
            if (tower != null) {
                canvas.drawCircle(tower.getCircleX(), tower.getCircleY(), 40, paint);
                mPaint.setColor(Color.RED);
                canvas.drawText((selTower + 1) + " 号炮台", drawX, drawY, mPaint);
                mPaint.setColor(Color.WHITE);
                canvas.drawText("攻击强度:" + tower.getDamage(), drawX, drawY + 50, mPaint);
                canvas.drawText("攻击间隔:" + tower.getAttackTimes(), drawX, drawY + 100, mPaint);
                canvas.drawText("攻击范围:" + tower.getAttackR(), drawX, drawY + 150, mPaint);
            }
        }
    }

    int selTower = 0;

    /** 画地图表格 */
    private void drawMap(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        // 画横线
        for (int i = 0; i <= LeMapDes.rows; i++) {
            canvas.drawLine(0, i * bgHeight, LeMapDes.cols * bgWidth, i * bgHeight, paint);
        }
        // 画竖线
        for (int i = 0; i <= LeMapDes.cols; i++) {
            canvas.drawLine(0 + (i * bgWidth), 0, (i * bgWidth), LeMapDes.rows * bgHeight, paint);
        }
        // 填充单元格颜色
        paint.setColor(Color.GREEN);
        paint.setAlpha(100);
        for (int i = 0; i < LeMapDes.rows; i++) {
            for (int j = 0; j < LeMapDes.cols; j++) {
                if (map[i][j] == -1) {
                    paint.setColor(Color.RED);
                } else if (map[i][j] == 0) {
                    paint.setColor(Color.GREEN);
                }
                paint.setAlpha(100);
                canvas.drawRect(bgWidth * (j), i * bgHeight, bgWidth * (j + 1), bgHeight * (i + 1), paint);
            }
        }
    }

    /** 获得的行和列及炮塔的坐标 */
    private void getTowerXY(float x, float y) {
        curRow = (int) (y / bgHeight);// 当前行
        curCol = (int) (x / bgWidth);// 当前列

        if (curCol >= LeMapDes.cols)
            curCol = LeMapDes.cols - 1;
        if (curCol <= 0) {
            curCol = 0;
        }
        if (curRow >= LeMapDes.rows)
            curRow = LeMapDes.rows - 1;
        if (curRow <= 0) {
            curRow = 0;
        }
        towerX = curCol * bgWidth;
        towerY = curRow * bgHeight;
    }

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 判断是否点击游戏菜单
                    if (mGameMenu.isGameMenuClicked(event.getX(), event.getY())) {
                        dragging = true;
                        if (mGameMenu.isClickItem()) {
                            itemSelected = mGameMenu.getSelectedItem();
                            // return false;
                        }
                    }
                    // 如果炮塔菜单可见并且点击之后则处理对应的事件
                    if (towerMenu.isVisibility()) {
                        if (towerMenu.isGameMenuClicked(event.getX(), event.getY())) {
                            towerUp();// 箭塔升级
                            return false;
                        }
                    }
                }
            case MotionEvent.ACTION_MOVE:
                if (dragging == true) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        float newX = event.getX() - mGameMenu.vWidth / 2;
                        float newY = event.getY() - mGameMenu.vHeight / 2;
                        mGameMenu.setMenuLocation(newX, newY);
                    }
                } else {
                    getTowerXY(event.getX(), event.getY());
                    isDrawMap = true;
                    // 判断当前位置是否有炮塔
                    if (map[curRow][curCol] == 1) {
                        // 遍历找到对应当前地图的炮塔
                        for (int i = 0; i < towers.size(); i++) {
                            if (curRow == towers.get(i).getRow() && curCol == towers.get(i).getCol()) {
                                // 显示攻击范围
                                towers.get(i).setDrawAttackR(true);
                                // 记录当前的炮塔
                                selectedTower = i;
                                // 点击的是炮塔所以取消绘制地图
                                isDrawMap = false;
                                // 以下是处理长按事件元素
                                mLastMotionX = (int) event.getX();
                                mLastMotionY = (int) event.getY();
                                // // 长按事件内部计数器子增
                                // mCounter++;
                                isReleased = false;
                                isMoved = false;
                                // postDelayed(mLongPressRunnable,
                                // ViewConfiguration.getLongPressTimeout());
                                postDelayed(mLongPressRunnable, 1000);
                                if (MotionEvent.ACTION_MOVE == event.getAction()) {
                                    if (isMoved) {
                                        break;
                                    }
                                    if (Math.abs(mLastMotionX - (int) event.getX()) > TOUCH_SLOP
                                            || Math.abs(mLastMotionY - (int) event.getY()) > TOUCH_SLOP) {
                                        // 移动超过阈值，则表示移动了
                                        isMoved = true;
                                    }
                                }
                            }
                        }
                    } else {
                        isDrawTower = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrawMap = false;
                isDrawTower = false;
                dragging = false;
                isReleased = true;

                if (map[curRow][curCol] == 0) {
                    addTowerInMap();
                    // 没有添加炮塔成功则把当前的行和列置0
                    if (map[curRow][curCol] == 0) {
                        curRow = 0;
                        curCol = 0;
                    }
                }
                // 取消绘制攻击半径
                for (int i = 0; i < towers.size(); i++) {
                    if (towers.get(i).isDrawAttackR()) {
                        towers.get(i).setDrawAttackR(false);
                    }
                }
        }
        return true;
    }

    /** 炮台升级 */
    public void towerUp() {
        Tower towerUp = towers.get(selectedTower);
        switch (towerMenu.getSelectedItem()) {
            case 0:// 升级攻击强度
                switch (towerUp.getTowerType()) {
                    case Tower.MulDamage:
                    case Tower.MulAttack:
                        if (momey >= 20) {
                            int damage = towerUp.getDamage();
                            if (towerUp.getTowerType() == Tower.MulDamage) {
                                if (damage < towerUp.MaxDamage * 2) {
                                    towerUp.setDamage(damage + 2);
                                    T.showShort(mContext, "提升强度成功！");
                                    momey -= 20;
                                }
                            } else {
                                if (damage < towerUp.MaxDamage) {
                                    towerUp.setDamage(damage + 1);
                                    T.showShort(mContext, "提升强度成功！");
                                    momey -= 20;
                                }
                            }
                        } else {
                            T.showShort(mContext, "金币不足！");
                        }
                        break;
                    case Tower.SLOW:
                    case Tower.NORMAL:
                        if (momey >= 10) {
                            int damage = towerUp.getDamage();
                            if (damage <= towerUp.MaxDamage) {
                                towerUp.setDamage(damage + 1);
                                momey -= 10;
                                T.showShort(mContext, "提升强度成功！");
                            }
                        } else {
                            T.showShort(mContext, "金币不足！");
                        }
                        break;
                }
                towerMenu.setVisibility(false);
                break;
            case 1:// 升级攻击范围
                switch (towerUp.getTowerType()) {
                    case Tower.SLOW:
                    case Tower.MulAttack:
                        if (momey >= 20) {
                            float attackR = towerUp.getAttackR();
                            if (attackR <= towerUp.MAXATTACKR) {
                                towerUp.setAttackR(attackR + 5f);
                                momey -= 20;
                                T.showShort(mContext, "提升范围成功！");
                            }
                        } else {
                            T.showShort(mContext, "金币不足！");
                        }
                        break;
                    case Tower.MulDamage:
                        if (momey >= 15) {
                            float attackR = towerUp.getAttackR();
                            if (attackR <= towerUp.MAXATTACKR) {
                                towerUp.setAttackR(attackR + 5f);
                                momey -= 15;
                                T.showShort(mContext, "提升范围成功！");
                            }
                        } else {
                            T.showShort(mContext, "金币不足！");
                        }
                        break;
                    case Tower.NORMAL:
                        if (momey >= 10) {
                            float attackR = towerUp.getAttackR();
                            if (attackR <= towerUp.MAXATTACKR) {
                                towerUp.setAttackR(attackR + 5f);
                                momey -= 10;
                                T.showShort(mContext, "提升范围成功！");
                            }
                        } else {
                            T.showShort(mContext, "金币不足！");
                        }
                        break;
                }
                towerMenu.setVisibility(false);
                break;
            case 2:// 攻击速度
                if (momey >= 20) {
                    int attackTime = towerUp.getAttackTimes();
                    if (attackTime - 100 >= towerUp.MinAttackTimes) {
                        towerUp.setAttackTimes(attackTime - 100);
                        momey -= 20;
                    }
                    T.showShort(mContext, "提升速度成功！");
                } else {
                    T.showShort(mContext, "金币不足！");
                }
                towerMenu.setVisibility(false);
                break;
            case 3:// 菜单消失即设置为不可见
                towerMenu.setVisibility(false);
            default:
                break;
        }

    }

    /** 添加怪物到地图 */
    public void addEnemyInMap() {
        Bitmap enemyBitmap;
        EnemyData data = mConst.loadEnemyData(enemyLevel);
        if (curEnemyCount <= data.num) {
            curEnemyCount++;
        }
        if (curEnemyCount > data.num) {
            // 当前敌人全部死完或则逃脱的时候才添加下一波敌人
            if (enemys.size() != 0) {
                return;
            }
            curEnemyCount = 1;
            enemyLevel++;
            data = mConst.loadEnemyData(enemyLevel);
        }
        enemyName = data.name;
        HP = data.hp;
        enemyMoveSpeed = data.moveSpeed;
        enemyBitmap = BitmapUtils.readAndResizeBitmap(mContext, data.id, bgWidth * 0.5f * 4, bgHeight * 0.5f * 4);
        Enemy tmp = new Enemy(mContext, enemyBitmap, enemyX, enemyY);
        // 设置敌人基本属性数据
        tmp.setEnemyData(data);
        Path mPath = LeMapPath.getPathByLevel(levelNum, bgWidth, bgHeight);
        // 设置敌人移动路径
        tmp.setMovePath(mPath);
        // 设置敌人编号
        tmp.setEnemyNum(enemyNum);
        enemyNum++;
        enemys.add(tmp);
    }

    /** 添加炮台到地图 */
    public void addTowerInMap() {
        Tower tmpTower;
        Bitmap bullet;
        tmpTower = new Tower(tower[itemSelected], towerX, towerY);
        tmpTower.setRow(curRow);
        tmpTower.setCol(curCol);

        switch (itemSelected) {
            case 0:// 箭塔
                if (momey >= 10) {
                    momey -= 10;
                    tmpTower.setDamage(5);
                    tmpTower.setAttackR(110 * 2f);
                    tmpTower.setAttackTimes(800);
                    bullet = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.game_bullet2, bgWidth / 2, 10);
                    tmpTower.setBulletBitmap(bullet);
                    tmpTower.setTowerType(Tower.NORMAL);

                    towers.add(tmpTower);
                    // map[curWz.row][curWz.col] = 1;
                    map[curRow][curCol] = 1;
                }
                break;
            case 1:// 炮台
                if (momey >= 15) {
                    momey -= 15;
                    tmpTower.setDamage(3);
                    tmpTower.setAttackR(100 * 2f);
                    tmpTower.setAttackTimes(1100);
                    bullet = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.bullet1, bgWidth / 2, 10);
                    tmpTower.setBulletBitmap(bullet);
                    tmpTower.setTowerType(Tower.MulAttack);
                    towers.add(tmpTower);
                    // map[curWz.row][curWz.col] = 1;
                    map[curRow][curCol] = 1;
                }
                break;
            case 2:// 减速魔法塔
                if (momey >= 20) {
                    momey -= 20;
                    tmpTower.setDamage(2);
                    tmpTower.setAttackR(100 * 2f);
                    tmpTower.setAttackTimes(1000);
                    bullet = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.bullet2, bgWidth / 3, bgHeight / 3);
                    tmpTower.setBulletBitmap(bullet);
                    tmpTower.setTowerType(Tower.SLOW);
                    towers.add(tmpTower);
                    // map[curWz.row][curWz.col] = 1;
                    map[curRow][curCol] = 1;
                }
                break;
            case 3:// 火箭炮
                if (momey >= 25) {
                    momey -= 25;
                    tmpTower.setDamage(12);
                    tmpTower.setAttackR(120 * 2f);
                    tmpTower.setAttackTimes(1500);
                    bullet = BitmapUtils.readAndResizeBitmap(mContext, R.drawable.rocket2, bgWidth / 2, bgHeight / 2);
                    tmpTower.setBulletBitmap(bullet);
                    tmpTower.setTowerType(Tower.MulDamage);
                    towers.add(tmpTower);
                    // map[curWz.row][curWz.col] = 1;
                    map[curRow][curCol] = 1;
                }
                break;
            default:
                break;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    /**
     * 怪物线程
     *
     * @author shaoshuai
     */
    class EnemyThread extends Thread {
        public void run() {
            boolean displayHp = false;// 显示血量
            Enemy enemy;
            while (running) {
                if (pause) {
                    continue;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();
                // 这个循环用于控制炮塔攻击时间间隔
                for (int k = 0; k < towers.size(); k++) {
                    if (towers.get(k).isMinAttackTimes()) {
                        if (towers.get(k).isminAttackSpeedTimeEnd()) {
                            int attackTimes = towers.get(k).getAttackTimesOrg();
                            towers.get(k).setAttackTimes(attackTimes);
                        }
                    }
                    towers.get(k).IncreaseTimes();
                }

                for (int i = 0; i < enemys.size(); i++) {
                    // 超出地图则移除敌人
                    enemy = enemys.get(i);
                    if (enemy.isRemove()) {
                        enemys.remove(enemy);
                        runAwayNum++;
                        continue;
                    } else {
                        enemy.moveByPath();
                    }

                    if (enemy.isSpeedSlow()) {
                        if (enemy.isSlowTimeEnd()) {
                            enemy.recoverSpeed();
                        }
                    }

                    if (enemy.isBlood()) {
                        if (enemy.isBloodTimeEnd()) {
                            if (enemy.getHP() <= 0) {
                                bombX = enemy.getX();
                                bombY = enemy.getY();
                                isDrawBomb = true;
                                momey += enemy.getKilledMoney();
                                enemys.remove(enemy);
                                continue;
                            }
                        }
                    }
                    // 走出了所有塔的攻击范围之后不再显示血条
                    for (int j = 0; j < towers.size(); j++) {
                        if (towers.get(j).isIntoAttackRange(enemy)) {
                            displayHp = true;
                            break;
                        } else {
                            displayHp = false;
                        }
                    }
                    if (displayHp == false && enemy.isBlood() == false) {
                        enemy.setDisplayHP(displayHp);
                    }
                }
                // 下一关
                if (enemys.size() == 0 && mConst.getEnemysSize() - 1 == enemyLevel) {
                    nextGame();
                    // continue;
                }
                if (enemys.size() == 0 || enemys.get(enemys.size() - 1).getY() >= bgHeight) {
                    addEnemyInMap();
                }
                if (runAwayNum >= 30) {
                    T.showShort(mContext, "防守失败! 再来一次...");
                    resetGame();
                }
                lock.unlock();
            }
        }
    }

    /**
     * 炮台线程
     *
     * @author shaoshuai
     */
    class TowerThread extends Thread {
        Random random = new Random();
        int enemyNum[] = new int[20];

        public void run() {
            Tower tower = null;
            Enemy tmpEnemy = null;
            while (running) {
                if (pause) {
                    continue;
                }
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();
                // 箭塔添加子弹
                towerAddBullet();

                for (int i = 0; i < towers.size(); i++) {
                    tower = towers.get(i);
                    for (int j = 0; j < tower.getBulletsSize(); j++) {
                        // 循环遍历子弹，找到每个子弹对应的敌人
                        final int enemyNum = tower.getBullet(j).getEnemyNum();
                        tmpEnemy = null;
                        for (int k = 0; k < enemys.size(); k++) {
                            if (enemyNum == enemys.get(k).getEnemyNum()) {
                                tmpEnemy = enemys.get(k);
                                break;
                            }
                        }
                        // 找不到对应的敌人则移除子弹
                        if (tmpEnemy == null) {
                            tower.removeBullet(j);
                            continue;
                        }
                        float enemyX = tmpEnemy.getCircleX();
                        float enemyY = tmpEnemy.getCircleY();
                        // 得到每个子弹对应的怪物坐标，子弹根据坐标移动
                        tower.getBullet(j).setEnemyX(enemyX);
                        tower.getBullet(j).setEnemyY(enemyY);
                        tower.getBullet(j).move();
                        // 检测子弹是否碰撞怪物
                        if (tower.getBullet(j).isCollidedEnemy(tmpEnemy)) {
                            int damage = tower.getBullet(j).getDamage();
                            // 是否双倍伤害
                            if (tower.isDoubleDamage()) {
                                int mul = tower.getMulDamage();
                                tmpEnemy.reduceHP(damage * mul);// 减血
                            } else {
                                tmpEnemy.reduceHP(damage);// 减血
                            }
                            // 是否是最小攻击速度
                            if (tower.isMinAttackSpeed()) {
                                int attackTimes = tower.getAttackTimes();
                                tower.setAttackTimesOrg(attackTimes);
                                tower.setAttackTimes(100);
                            }
                            // 炮台类型是否多重攻击炮台
                            if (tower.getTowerType() == Tower.MulAttack) {
                                if (random.nextInt(100) < 10) {
                                    tmpEnemy.setBlood(true);// 设置为流血状态
                                }
                            }
                            // 炮台类型是否为减速魔法塔
                            if (tower.isSlow()) {
                                float speed = tmpEnemy.getMoveSpeedOrg();
                                if (tower.getAttackTimes() == tower.MinAttackTimes) {
                                    if (random.nextInt(100) <= 20) {
                                        tmpEnemy.setMoveSpeed(0);
                                    }
                                } else {
                                    if (random.nextInt(100) <= 5) {
                                        tmpEnemy.setMoveSpeed(0);
                                    } else {
                                        if (tmpEnemy.getMoveSpeed() != 0) {
                                            tmpEnemy.setMoveSpeed(speed / 2);
                                            tmpEnemy.StartSlow();
                                        }
                                    }
                                }
                            }
                            tower.removeBullet(j);// 子弹碰撞怪物则移除子弹
                            tmpEnemy.setDisplayHP(true);// 是否显示怪物血条
                            // 怪物生命值小于等于0则移除怪物
                            if (tmpEnemy.getHP() <= 0) {
                                adjustBullets(enemyNum);// 移除怪物的时候要把对应当前怪物的子弹移除
                                // 根据当前的敌人坐标绘制爆炸效果
                                bombX = tmpEnemy.getX();
                                bombY = tmpEnemy.getY();
                                isDrawBomb = true;// 绘制炸弹
                                killNum++;// 杀敌数
                                momey += tmpEnemy.getKilledMoney();// 金钱
                                enemys.remove(tmpEnemy);// 移除敌人
                            }
                        } else if (towers.get(i).isBulletOutOfAttackR(j)) {
                            towers.get(i).removeBullet(j);// 子弹超出攻击范围则移除
                        }
                    }
                }
                lock.unlock();
            }
        }

        private void adjustBullets(int enemyNum) {
            for (int i = 0; i < towers.size(); i++) {
                for (int j = 0; j < towers.get(i).getBulletsSize(); j++) {
                    if (enemyNum == towers.get(i).getBullet(j).getEnemyNum()) {
                        towers.get(i).removeBullet(j);
                    }
                }
            }
        }

        /** 炮台添加子弹 */
        private void towerAddBullet() {
            int index = 0;
            Enemy enemy;
            Tower tower;
            for (int i = 0; i < towers.size(); i++) {
                tower = towers.get(i);
                index = 0;
                // 判断炮塔攻击间隔时间是否到x
                if (tower.getTimesCount() >= tower.getAttackTimes()) {
                    // 记录所有进入炮塔攻击范围的敌人
                    for (int j = 0; j < enemys.size(); j++) {
                        enemy = enemys.get(j);
                        if (tower.isIntoAttackRange(enemy)) {
                            enemyNum[index] = enemy.getEnemyNum();
                            index++;
                            if (tower.getTowerType() == Tower.MulAttack) {
                                if (tower.getBulletsSize() < tower.getMulAttackNum()) {
                                    tower.addBullet(enemy.getCircleX(), enemy.getCircleY(), enemy.getEnemyNum());
                                    tower.setTimesCount(0);// 攻击间隔清0
                                }
                            }
                        }
                    }
                    if (index > 0) {
                        // 减速炮塔随机攻击进入攻击范围的敌人
                        if (tower.getTowerType() == Tower.SLOW) {
                            int rand = random.nextInt(index);
                            for (int j = 0; j < enemys.size(); j++) {
                                enemy = enemys.get(j);
                                if (enemyNum[rand] == enemy.getEnemyNum()) {
                                    tower.addBullet(enemy.getCircleX(), enemy.getCircleY(), enemyNum[rand]);
                                }
                            }
                        } else if (tower.getTowerType() != Tower.MulAttack) {
                            for (int j = 0; j < enemys.size(); j++) {
                                enemy = enemys.get(j);
                                if (enemyNum[0] == enemy.getEnemyNum()) {
                                    tower.addBullet(enemy.getCircleX(), enemy.getCircleY(), enemyNum[0]);
                                }
                            }
                        }
                        tower.setTimesCount(0);
                    }
                }
            }
        }
    }
}
