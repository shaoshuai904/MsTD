package com.maple.mstd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.maple.mstd.R;
import com.maple.mstd.ui.custom.GameView;

/**
 * 游戏界面
 *
 * @author shaoshuai
 */
public class GameActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.gameview)
    private GameView mGameView;// 游戏视图

    @ViewInject(R.id.bt_renew)
    private Button bt_renew;// 重新开始
    @ViewInject(R.id.bt_pause)
    private Button bt_pause;// 暂停
    @ViewInject(R.id.bt_next)
    private Button bt_next;// 下一关
    @ViewInject(R.id.bt_exit)
    private Button bt_exit;// 退出

    public static final String IN_LOAGING_PAGE_INDEX = "intent_load_page_index";// 需要加载的页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        ViewUtils.inject(this);

        bt_renew.setOnClickListener(this);
        bt_pause.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        bt_exit.setOnClickListener(this);

        Intent it = getIntent();
        int index = it.getIntExtra(IN_LOAGING_PAGE_INDEX, 0);
        mGameView.setGameLevel(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_renew:// 重新开始
                mGameView.resetGame();
                break;
            case R.id.bt_pause:// 暂停继续
                if (!mGameView.isPause()) {
                    mGameView.setPause(true);
                } else {
                    mGameView.setPause(false);
                }
                break;
            case R.id.bt_next:// 下一关
                mGameView.nextGame();
                break;
            case R.id.bt_exit:// 退出
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mGameView.setRunning(false);
        super.onDestroy();
    }

}
