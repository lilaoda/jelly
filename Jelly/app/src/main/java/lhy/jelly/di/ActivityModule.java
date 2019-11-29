package lhy.jelly.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import lhy.jelly.ui.login.LoginActivity;
import lhy.jelly.ui.main.MainActivity;
import lhy.jelly.ui.main.MainModule;
import lhy.jelly.ui.music.MusicPlayActivity;
import lhy.jelly.ui.video.PlayVideoActivity;
import lhy.jelly.ui.video.PlayVidoActivity;

/**
 * Created by Lilaoda on 2018/3/30.
 * Email:749948218@qq.com
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract LoginActivity loginActivityInjector();

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivityInjector();

    @ContributesAndroidInjector
    abstract MusicPlayActivity musicPlayActivityInjector();

    @ContributesAndroidInjector
    abstract PlayVidoActivity playVidoActivityActivity();

    @ContributesAndroidInjector
    abstract PlayVideoActivity playVideoActivityActivity();
}
