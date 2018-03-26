package lhy.jelly;

import android.content.Context;
import android.support.multidex.MultiDex;

import lhy.lhylibrary.base.LhyApplication;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

public class JellyApplicaiton extends LhyApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        applicationComponent = DaggerApplicationComponent.builder()
//                .repositoryModule(new RepositoryModule(this))
//                .applicationModule(new ApplicationModule(this))
//                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}