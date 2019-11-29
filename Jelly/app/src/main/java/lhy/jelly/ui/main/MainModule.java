package lhy.jelly.ui.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;

/**
 * Created  on 2019/2/27 15:39
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
@Module
public abstract class MainModule {

    @ContributesAndroidInjector
    abstract MusicFragment musicFragmentInjector();

    @ContributesAndroidInjector
    abstract MineFragment mineFragmentInjector();

    @ContributesAndroidInjector
    abstract VideoFragment videoFragmentInjector();

    @ContributesAndroidInjector
    abstract ChatFragment chatFragmentInjector();
}
