package com.ms_td.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ms_td.R;

/**
 * 主界面
 * 
 * @author shaoshuai
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.bt_start_game)
	private Button bt_start_game;// 快速开始

	@ViewInject(R.id.bt_sel_level1)
	private Button bt_sel_level1;// 关卡1
	@ViewInject(R.id.bt_sel_level2)
	private Button bt_sel_level2;// 关卡2
	@ViewInject(R.id.bt_sel_level3)
	private Button bt_sel_level3;// 关卡3
	@ViewInject(R.id.bt_sel_level4)
	private Button bt_sel_level4;// 关卡4

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);

		bt_start_game.setOnClickListener(this);
		bt_sel_level1.setOnClickListener(this);
		bt_sel_level2.setOnClickListener(this);
		bt_sel_level3.setOnClickListener(this);
		bt_sel_level4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start_game:// 开始游戏
			toActivPage(1);
			break;
		case R.id.bt_sel_level1:// 选择关卡
			toActivPage(1);
			break;
		case R.id.bt_sel_level2:// 选择关卡
			toActivPage(2);
			break;
		case R.id.bt_sel_level3:// 选择关卡
			toActivPage(3);
			break;
		case R.id.bt_sel_level4:// 选择关卡
			toActivPage(4);
			break;
		default:
			break;
		}
	}

	/** 去活动界面 */
	private void toActivPage(int levelNum) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(GameActivity.IN_LOAGING_PAGE_INDEX, levelNum);// 关卡
		startActivity(intent);

	}
}
