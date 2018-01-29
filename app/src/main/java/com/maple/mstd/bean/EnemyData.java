package com.maple.mstd.bean;

/**
 * 怪物Item
 *
 * @author shaoshuai
 */
public class EnemyData {

    public final String name; // 名字
    public final int hp; // 血量
    public final float moveSpeed; // 速度
    public final int killedMoney; // 杀死后获得金币
    public final int animTime; // 帧动画时间, 移动速度不同帧动画时间也就不同
    public final int num; // 数量
    public final int id; // 图片id

    public EnemyData(String name, int hp, int moveSpeed, int killedMoney, int animTime, int num, int drawId) {
        this.name = name;
        this.hp = hp;
        this.moveSpeed = moveSpeed;
        this.killedMoney = killedMoney;
        this.animTime = animTime;
        this.num = num;
        this.id = drawId;
    }
}
