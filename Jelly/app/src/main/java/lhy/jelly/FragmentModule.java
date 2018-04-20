package lhy.jelly;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import lhy.jelly.ui.main.MainActivity;
import lhy.jelly.ui.music.MusicFragment;

/**
 * Created by Lilaoda on 2018/3/30.
 * Email:749948218@qq.com
 */

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MusicFragment musicFragmentInjector();
}
