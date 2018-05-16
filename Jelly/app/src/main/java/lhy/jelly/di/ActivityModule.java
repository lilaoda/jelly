package lhy.jelly.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import lhy.jelly.ui.login.LoginActivity;
import lhy.jelly.ui.main.MainActivity;

/**
 * Created by Lilaoda on 2018/3/30.
 * Email:749948218@qq.com
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract LoginActivity loginActivityInjector();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity mainActivityInjector();
}
