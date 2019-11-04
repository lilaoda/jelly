package lhy.jelly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import lhy.lhylibrary.base.LhyActivity;

/**
 * Created  on 2019/1/18 10:48
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public abstract class BaseActivity extends LhyActivity implements HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @Inject
    DispatchingAndroidInjector<android.app.Fragment> fragmentInjector;

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


    @Override
    public AndroidInjector<android.app.Fragment> fragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public AndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }
}
