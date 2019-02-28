package lhy.jelly.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lhy.jelly.data.DbManager;
import lhy.jelly.data.HttpManager;
import lhy.jelly.data.local.gen.UserDao;
import lhy.jelly.data.remote.ApiService;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 *
 * @author lhy
 */

@Singleton
@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    public Context provideContext(Application applicaiton) {
        return applicaiton;
    }

    @Singleton
    @Provides
    public DbManager provideDbManager(Context context) {
        return DbManager.getInstance(context);
    }

    @Singleton
    @Provides
    public ApiService provideApiService() {
        return HttpManager.getInstance().getApiService();
    }

    @Singleton
    @Provides
    public UserDao provideUserDao(DbManager dbManager) {
        return dbManager.getUserDao();
    }

}
