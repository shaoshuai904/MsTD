package com.ms_td;

import com.ms_td.bean.EnemyData;

/**
 * 怪物工厂
 * 
 * @author shaoshuai
 * 
 */
public class EnemyFactory {

	private EnemyData[] mEnemys = {
			// ------------名称----生命值--移动速度-金钱-动画时间-数量---图片Id
			new EnemyData("斧头地精", 20, 02 * 3, 01, 200, 25, R.drawable.enemy10),
			new EnemyData("棒槌地精", 30, 02 * 3, 01, 200, 30, R.drawable.enemy11),
			new EnemyData("狂暴地精", 40, 03 * 3, 02, 180, 20, R.drawable.enemy12),
			new EnemyData("食  尸  鬼", 60, 03 * 3, 02, 180, 20, R.drawable.enemy1),
			new EnemyData("女  拳  师", 80, 03 * 3, 02, 180, 20, R.drawable.enemy5),
			new EnemyData("女  神  官", 60, 04 * 3, 03, 160, 20, R.drawable.enemy3),
			new EnemyData("女  剑  士", 100, 03 * 3, 03, 180, 20, R.drawable.enemy),
			new EnemyData("男  剑  士", 120, 04 * 3, 04, 160, 30, R.drawable.enemy6),
			new EnemyData("疾风忍者", 100, 07 * 3, 07, 100, 20, R.drawable.enemy21),
			new EnemyData("铠甲勇士", 1000, 01 * 3, 10, 220, 10, R.drawable.enemy20),
			new EnemyData("圣斗士穆", 200, 04 * 3, 03, 160, 15, R.drawable.enemy9),
			new EnemyData("双子加隆", 500, 02 * 3, 05, 160, 20, R.drawable.enemy2),
			new EnemyData(" 沙  加  ", 275, 04 * 3, 04, 160, 20, R.drawable.enemy15),
			new EnemyData(" 史  昂  ", 400, 03 * 3, 05, 160, 20, R.drawable.enemy16),
			new EnemyData(" 米  罗  ", 450, 04 * 3, 06, 160, 20, R.drawable.enemy17),
			new EnemyData(" 卡  妙  ", 500, 04 * 3, 07, 180, 20, R.drawable.enemy18),
			new EnemyData(" 盗  贼  ", 300, 06 * 3, 10, 180, 20, R.drawable.enemy22),
			new EnemyData("阿布罗迪", 600, 04 * 3, 10, 180, 20, R.drawable.enemy19),
			new EnemyData(" 修  罗  ", 1200, 02 * 3, 12, 180, 20, R.drawable.enemy23),
			new EnemyData(" Boss", 5000, 05 * 3, 20, 140, 1, R.drawable.enemy7) };

	/** 获取第几波怪物 */
	public EnemyData loadEnemyData(int index) {
		if (index >= mEnemys.length) {
			index = mEnemys.length - 1;
		}
		return mEnemys[index];
	}

	/** 获取怪物种类 */
	public int getEnemysSize() {
		return mEnemys.length;
	}
}
