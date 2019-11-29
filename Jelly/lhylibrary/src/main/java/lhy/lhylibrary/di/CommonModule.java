package lhy.lhylibrary.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lhy.lhylibrary.http.OkHttpManager;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 *
 * @author lhy
 */

@Module
public class CommonModule {


    @Singleton
    @Provides
    public OkHttpManager provideApplication() {
        return OkHttpManager.getInstance();
    }

}
