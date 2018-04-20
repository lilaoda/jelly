package lhy.jelly;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lhy.jelly.data.local.entity.User;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 */


@Module
public class AppModule {
//    public AppModule() {
//
//    }
    //不加singleton不是单例
    @Singleton
    @Provides
    public User provideUser() {
        return new User();
    }
}
