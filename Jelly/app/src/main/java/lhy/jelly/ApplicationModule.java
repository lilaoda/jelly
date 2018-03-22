package lhy.jelly;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lhy.jelly.data.local.entity.User;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

@Singleton
@Module
public final class ApplicationModule {

    private final Context context;
    private User user;

    public ApplicationModule(Context context) {
        this.context = context;
    }


    @Singleton
    @Provides
   // @ForReleasableReferences(ActivityScoped.class)
    //标记在activityScoped里该实例为弱引用，而不是强引用
    public Context provideContext() {
        return context;
    }

    //不加singleton不是单例
    @Singleton
    @Provides
    public User provideUser() {
        return new User();
    }
}
