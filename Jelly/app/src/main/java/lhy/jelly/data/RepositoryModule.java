package lhy.jelly.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lhy.jelly.data.remote.ApiService;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */
@Singleton
@Module
public class RepositoryModule {

    private final Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public DbManager provideDbManager() {
        return DbManager.getInstance(context);
    }

    @Singleton
    @Provides
    public ApiService provideApiService() {
        return HttpManager.getInstance().getApiService();
    }
}
