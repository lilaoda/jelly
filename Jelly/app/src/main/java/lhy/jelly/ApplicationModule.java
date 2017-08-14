package lhy.jelly;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}
