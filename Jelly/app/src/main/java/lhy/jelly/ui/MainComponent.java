package lhy.jelly.ui;

import dagger.Component;
import lhy.jelly.ActivityScoped;
import lhy.jelly.ApplicationComponent;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class,modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
