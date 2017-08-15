package lhy.jelly.ui;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

@Module
public class MainModule {

    private final MainActivity mainActivity;

    public MainModule(MainActivity activity) {
        mainActivity = activity;
    }

    @Provides
    MainActivity provideMainView() {
        return mainActivity;
    }
}
