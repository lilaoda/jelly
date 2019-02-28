package lhy.jelly.base;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import dagger.internal.Beta;

/**
 * Created  on 2019/2/27 15:30
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
@Beta
public class AbstractDiFragment extends BaseFragment implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
}
