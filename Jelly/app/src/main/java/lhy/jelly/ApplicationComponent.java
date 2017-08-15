package lhy.jelly;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import lhy.jelly.data.DbManager;
import lhy.jelly.data.RepositoryModule;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.data.remote.ApiService;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

@Singleton
@Component(modules = {RepositoryModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    Context getContext();

    ApiService getApiService();

    DbManager getDbManager();

    User getUser();
}
