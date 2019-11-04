package lhy.lhylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.ref.WeakReference;

import lhy.lhylibrary.activity.OnePixelActivity;

/**
 * Created by Lilaoda on 2017/11/24.
 * Email:749948218@qq.com
 */

public class ScreenManager {

    private static final String TAG = "ScreenManager";
    private Context mContext;
    private static ScreenManager mScreenManager;
    private WeakReference<Activity> mActivityRef;

    private ScreenManager(Context mContext) {
        this.mContext = mContext;
    }

    public static ScreenManager getScreenManagerInstance(Context context) {
        if (mScreenManager == null) {
            mScreenManager = new ScreenManager(context);
        }
        return mScreenManager;
    }

    public void setSingleActivity(Activity mActivity) {
        mActivityRef = new WeakReference<>(mActivity);
    }

    public void startActivity() {
        Log.d(TAG, "准备启动1像素页面...");
        Intent intent = new Intent(mContext, OnePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    // 结束SinglePixelActivity
    public void finishActivity() {
        Log.d(TAG, "准备结束1像素页面...");
        if (mActivityRef != null) {
            Activity mActivity = mActivityRef.get();
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }
}