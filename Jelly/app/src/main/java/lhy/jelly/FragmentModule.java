package lhy.jelly;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;

/**
 * Created by Lilaoda on 2018/3/30.
 * Email:749948218@qq.com
 */

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MusicFragment musicFragmentInjector();

    @ContributesAndroidInjector
    abstract MineFragment mineFragmentInjector();

    @ContributesAndroidInjector
    abstract VideoFragment videoFragmentInjector();

    @ContributesAndroidInjector
    abstract ChatFragment chatFragmentInjector();
}
