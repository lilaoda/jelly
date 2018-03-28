package lhy.jelly;

import android.content.Context;
import android.support.multidex.MultiDex;

import lhy.lhylibrary.base.LhyApplication;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

public class JellyApplicaiton extends LhyApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}