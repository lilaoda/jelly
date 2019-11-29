package lhy.lhylibrary.di;

/**
 * Created by Lilaoda on 2018/3/29.
 * Email:749948218@qq.com
 */

public class CommonInjector {
//
//    private final Application mApplication;
//    private CommonComponent mCommonComponent;
//
//    public CommonInjector(Application application) {
//        mApplication = application;
//    }
//
//
//    public void init() {
//        mCommonComponent = DaggerCommonComponent.builder().commonModule(new CommonModule(mApplication)).build();
//        mCommonComponent.inject(this);
//        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                handleActivity(activity);
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });
//    }
//
//    private static void handleActivity(Activity activity) {
//        if (activity instanceof HasSupportFragmentInjector) {
//            AndroidInjection.inject(activity);
//        }
//        if (activity instanceof FragmentActivity) {
//            ((FragmentActivity) activity).getSupportFragmentManager()
//                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
//
//                        @Override
//                        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
//                            super.onFragmentAttached(fm, f, context);
//                            if (f instanceof Injectable) {
//                                AndroidSupportInjection.inject(f);
//                            }
//                        }
//                    }, true);
//        }
//    }
//
//    public CommonComponent getmCommonComponent() {
//        return mCommonComponent;
//    }
}