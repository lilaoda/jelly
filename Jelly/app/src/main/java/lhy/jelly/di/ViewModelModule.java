package lhy.jelly.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;



import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import lhy.jelly.ui.music.MusicModel;
import lhy.jelly.ui.video.VideoModel;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MusicModel.class)
    abstract ViewModel bindUserViewModel(MusicModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VideoModel.class)
    abstract ViewModel bindSearchViewModel(VideoModel videoModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(JellyViewModelFactory factory);
}
