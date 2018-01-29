package com.maple.mstd.ui.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;


/**
 * 功能菜单
 *
 * @author shaoshuai
 */
public class FunctionMenu {
    public float vWidth; // 菜单宽和高
    public float vHeight;

    public float menuX = 0; // 菜单坐标
    public float menuY = 0;

    float twoItemJianGe = 0;// 两个Item之间的间隔
    float leftAndRightBian = 0;

    /** 当前选中条目 */
    int selItem = 0;
    /** Item是否被点击 */
    boolean isClickItem = false;
    /** 是否可见 */
    boolean isVisibility = true;

    /** item列表 */
    private ArrayList<FunctionMenuItem> MenuItems;

    /**
     * 功能菜单
     *
     * @param width
     * @param height
     * @param mItems
     */
    public FunctionMenu(float width, float height, ArrayList<FunctionMenuItem> mItems) {
        this.vWidth = width;
        this.vHeight = height;
        setMenuItems(mItems);
        // 设置默认位置，如果在绘制之前忘记调用。则孩子都挤在左上角
        setMenuLocation(0, 0);
    }

    /** 设置内容数据 */
    public void setMenuItems(ArrayList<FunctionMenuItem> mItems) {
        this.MenuItems = mItems;
        // 计算大小
        float wbian = vWidth - mItems.get(0).vWidth;// 宽度差异
        float hbian = vHeight - mItems.get(0).vHeight * mItems.size();// 高度差异
        if (wbian > 0)
            leftAndRightBian = wbian / 2;// 增加左右边距
        if (hbian > 0)
            twoItemJianGe = hbian / (mItems.size() + 1);// 增加上下间隔
    }

    /** 获取内容数据 */
    public ArrayList<FunctionMenuItem> getMenuItems() {
        return MenuItems;
    }

    /** 绘制菜单 */
    public void drawGameMenu(Canvas canvas, float x, float y) {
        setMenuLocation(x, y);
        drawGameMenu(canvas);
    }

    /** 绘制菜单 */
    public void drawGameMenu(Canvas canvas) {
        if (isVisibility) {
            Paint menuPaint = new Paint();
            menuPaint.setColor(Color.BLACK);
            menuPaint.setAlpha(100);

            RectF mRect = new RectF(menuX, menuY, menuX + vWidth, menuY + vHeight);// 菜单栏所占范围
            canvas.drawRect(mRect, menuPaint);// 画矩形
            for (int i = 0; i < MenuItems.size(); i++) {
                MenuItems.get(i).drawMenuItem(canvas);// 画条目
                if (selItem == i) {// 选中
                    MenuItems.get(i).drawItemSelected(canvas);
                }
            }
        }
    }

    /** 设置菜单的x和y坐标 */
    public void setMenuLocation(float menuX, float menuY) {
        if (menuX + vWidth / 2 >= 480 * 2) {
            menuX = 480 * 2 - vWidth / 2;
        }
        if (menuY + vHeight / 2 >= 800 * 2) {
            menuY = 800 * 2 - vHeight / 2;
        }
        this.menuX = menuX;
        this.menuY = menuY;
        // 设置孩子位置
        for (int i = 0; i < MenuItems.size(); i++) {
            float x = menuX + leftAndRightBian;
            float y = MenuItems.get(i).vHeight * i + menuY + twoItemJianGe * (i + 1);
            MenuItems.get(i).setDrawXY(x, y);
        }
    }

    /** 判断是否点击菜单 */
    public boolean isGameMenuClicked(float x, float y) {
        if (menuX <= x && x <= menuX + vWidth && menuY <= y && y <= menuY + vHeight) {
            for (int i = 0; i < MenuItems.size(); i++) {
                if (MenuItems.get(i).itemClicked(x, y)) {
                    selItem = i;
                    isClickItem = true;
                    return true;
                }
            }
            isClickItem = false;
            return true;
        }
        return false;
    }

    // ------------------get-set----------------------------------

    /** 获取选中的条目 */
    public int getSelectedItem() {
        return selItem;
    }

    /** 设置选中条目 */
    public void setSelectedItem(int selectedItem) {
        this.selItem = selectedItem;
    }

    public boolean isClickItem() {
        return isClickItem;
    }

    public void setClickItem(boolean isClickItem) {
        this.isClickItem = isClickItem;
    }

    public boolean isVisibility() {
        return isVisibility;
    }

    public void setVisibility(boolean isVisibility) {
        this.isVisibility = isVisibility;
    }

}
