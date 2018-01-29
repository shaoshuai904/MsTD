package com.maple.mstd.bean;

/**
 * 位置
 *
 * @author shaoshuai
 */
public class WeiZhi {
    /** 所在行 */
    public int row = 0;
    /** 所在列 */
    public int col = 0;

    public WeiZhi() {
        super();
    }

    public WeiZhi(int row, int col) {
        super();
        this.row = row;
        this.col = col;
    }

    /**
     * 修正值范围
     *
     * @param maxRow - 最大行值
     * @param maxCol - 最大列值
     */
    public void reviseValue(int maxRow, int maxCol) {
        // row = row > maxRow ? maxRow : row;
        // 修正行
        if (row > maxRow)
            row = maxRow;
        if (row < 0) {
            row = 0;
        }
        // 修正列
        if (col > maxCol)
            col = maxCol;
        if (col < 0) {
            col = 0;
        }
    }

    /** 判断位置是否相同 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeiZhi wz = (WeiZhi) obj;
        return (row == wz.row && col == wz.col);
    }

}
