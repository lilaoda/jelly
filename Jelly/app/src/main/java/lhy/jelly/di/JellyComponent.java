package lhy.jelly.di;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import lhy.jelly.base.JellyApplication;
import lhy.lhylibrary.di.CommonComponent;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 */

@AppScoped
@Component(
        dependencies = CommonComponent.class,
        modules = {
                AndroidInjectionModule.class,
                AndroidSupportInjectionModule.class,
                ActivityModule.class,
                AppModule.class,
        })
public interface JellyComponent {

    void inject(JellyApplication application);
}
