package com.maple.mstd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maple.mstd.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面
 *
 * @author shaoshuai
 */
public class MainActivity extends Activity implements OnClickListener {
    @BindView(R.id.bt_start_game) Button bt_start_game;// 快速开始

    @BindView(R.id.bt_sel_level1) Button bt_sel_level1;// 关卡1
    @BindView(R.id.bt_sel_level2) Button bt_sel_level2;// 关卡2
    @BindView(R.id.bt_sel_level3) Button bt_sel_level3;// 关卡3
    @BindView(R.id.bt_sel_level4) Button bt_sel_level4;// 关卡4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

    /**
     * 去活动界面
     */
    private void toActivPage(int levelNum) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(GameActivity.IN_LOADING_PAGE_INDEX, levelNum);// 关卡
        startActivity(intent);

    }
}
