package lhy.jelly.di;

import android.app.Application;

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

@Module(includes = ViewModelModule.class)
public class AppModule {

    @AppScoped
    @Provides
    public DbManager provideDbManager(Application context) {
        return DbManager.getInstance(context);
    }

    @AppScoped
    @Provides
    public ApiService provideApiService() {
        return HttpManager.getInstance().getApiService();
    }

    @AppScoped
    @Provides
    public UserDao provideUserDao(DbManager dbManager) {
        return dbManager.getUserDao();
    }

}
