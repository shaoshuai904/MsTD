package com.maple.mstd;

import java.util.Random;

import com.maple.mstd.game.Path;

/**
 * 怪物行走的地图路径
 * 
 * @author shaoshuai
 * 
 */
public class LeMapPath {

	/**
	 * 获取怪物的行走路径
	 * 
	 * @param levelNum
	 *            - 关卡数
	 * @param bgWidth
	 *            - 单元格宽
	 * @param bgHeight
	 *            - 单元格高
	 * @return
	 */
	public static Path getPathByLevel(int levelNum, float bgWidth, float bgHeight) {
		Path mPath;
		switch (levelNum) {
		case 1:// 第一张地图路径// 随机3个路径
			int index = new Random().nextInt(3);
			switch (index) {
			case 0:
				mPath = new Path(1)//
						.to(5.5f * bgWidth, 22f * bgHeight);
				return mPath;
			case 1:
				mPath = new Path(5)//
						.to(5.5f * bgWidth, 1.5f * bgHeight)//
						.to(3.5f * bgWidth, 1.5f * bgHeight)//
						.to(3.5f * bgWidth, 19.5f * bgHeight)//
						.to(5.5f * bgWidth, 19.5f * bgHeight)//
						.to(5.5f * bgWidth, 22f * bgHeight);
				return mPath;
			case 2:
				mPath = new Path(5)//
						.to(5.5f * bgWidth, 1.5f * bgHeight)//
						.to(7.5f * bgWidth, 1.5f * bgHeight)//
						.to(7.5f * bgWidth, 19.5f * bgHeight)//
						.to(5.5f * bgWidth, 19.5f * bgHeight)//
						.to(5.5f * bgWidth, 22f * bgHeight);
				return mPath;
			}
			break;
		case 2:
			mPath = new Path(12)//
					.to(9.5f * bgWidth, 4.5f * bgHeight)//
					.to(4.5f * bgWidth, 4.5f * bgHeight)//
					.to(4.5f * bgWidth, 9.5f * bgHeight)//
					.to(11.5f * bgWidth, 9.5f * bgHeight)//
					.to(11.5f * bgWidth, 14.5f * bgHeight)//
					.to(7.5f * bgWidth, 14.5f * bgHeight)//
					.to(7.5f * bgWidth, 12.5f * bgHeight)//
					.to(4.5f * bgWidth, 12.5f * bgHeight)//
					.to(4.5f * bgWidth, 16.5f * bgHeight)//
					.to(3.5f * bgWidth, 16.5f * bgHeight)//
					.to(3.5f * bgWidth, 19.5f * bgHeight)//
					.to(13f * bgWidth, 19.5f * bgHeight);
			return mPath;
		case 3:
			mPath = new Path(11)//
					.to(0.8f * bgWidth, 4.5f * bgHeight)//
					.to(1.7f * bgWidth, 4.5f * bgHeight)//
					.to(1.7f * bgWidth, 8.0f * bgHeight)//
					.to(2.5f * bgWidth, 8.0f * bgHeight)//
					.to(2.5f * bgWidth, 13.5f * bgHeight)//
					.to(3.5f * bgWidth, 13.5f * bgHeight)//
					.to(3.5f * bgWidth, 16.5f * bgHeight)//
					.to(9.0f * bgWidth, 16.5f * bgHeight)//
					.to(9.0f * bgWidth, 13.5f * bgHeight)//
					.to(11.3f * bgWidth, 13.5f * bgHeight)//
					.to(11.3f * bgWidth, 6.0f * bgHeight);
			return mPath;

		case 4:
			mPath = new Path(10)//
					.to(3.5f * bgWidth, 4.5f * bgHeight)//
					.to(5.2f * bgWidth, 4.5f * bgHeight)//
					.to(5.2f * bgWidth, 12.5f * bgHeight)//
					.to(3.9f * bgWidth, 12.5f * bgHeight)//
					.to(3.9f * bgWidth, 9.0f * bgHeight)//
					.to(0.9f * bgWidth, 9.0f * bgHeight)//
					.to(0.9f * bgWidth, 16.8f * bgHeight)//
					.to(8.2f * bgWidth, 16.8f * bgHeight)//
					.to(8.2f * bgWidth, 13.5f * bgHeight)//
					.to(11.0f * bgWidth, 13.5f * bgHeight);
			return mPath;

		}
		return null;
	}

}
