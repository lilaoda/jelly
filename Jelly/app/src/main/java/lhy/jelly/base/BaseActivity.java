package lhy.jelly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import lhy.lhylibrary.base.LhyActivity;

/**
 * Created  on 2019/1/18 10:48
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class BaseActivity extends LhyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUpush();
    }

    private void initUpush() {
        //统计应用启动数据
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
