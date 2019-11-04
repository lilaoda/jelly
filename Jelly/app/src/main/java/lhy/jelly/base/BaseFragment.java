package lhy.jelly.base;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import lhy.jelly.di.Injectable;
import lhy.lhylibrary.base.LhyFragment;

/**
 * Created  on 2019/1/18 10:35
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class BaseFragment extends LhyFragment implements Injectable, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
