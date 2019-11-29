package lhy.lhylibrary.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 */


@Singleton
@Component(modules = {
        CommonModule.class,
})
public interface CommonComponent {
    Application app();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        CommonComponent build();
    }

}
