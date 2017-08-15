package lhy.jelly;

import lhy.jelly.data.RepositoryModule;
import lhy.lhylibrary.base.BaseApplication;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

public class JellyApplicaiton extends BaseApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .repositoryModule(new RepositoryModule(this))
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}