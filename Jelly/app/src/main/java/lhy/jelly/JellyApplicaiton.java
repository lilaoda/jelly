package lhy.jelly;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import lhy.lhylibrary.base.LhyApplication;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

public class JellyApplicaiton extends LhyApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}