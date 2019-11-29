package lhy.lhylibrary.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import lhy.lhylibrary.R;
import lhy.lhylibrary.utils.ScreenManager;
import lhy.lhylibrary.utils.ScreenReceiveUtils;

/**
 * Created by Lilaoda on 2017/11/22.
 * Email:749948218@qq.com
 * 保活服务 双重保活，1，双重服务将服务设置为前台服务，2，监听是否锁屏开启1像素保活Activity 3,服务中一直播放无声音乐，待完成
 */

public abstract class AliveService extends Service implements ScreenReceiveUtils.ScreenStateListener {

    private static final int NOTIFY_ID = 0x11;
    private ScreenReceiveUtils mScreenReceiveUtils;
    private ScreenManager mScreenManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.loading)
                .build();
        startForeground(NOTIFY_ID, notification);
        startService(new Intent(this, InnerService.class));

        mScreenReceiveUtils = new ScreenReceiveUtils(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenReceiveUtils.setScreenReceiverListener(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScreenReceiveUtils.stopScreenReceiverListener();
    }

    @Override
    public void onScreenOn() {
        mScreenManager.finishActivity();
    }

    @Override
    public void onScreenOff() {
        mScreenManager.startActivity();
    }

    @Override
    public void onUserPresent() {

    }

    public static class InnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.loading)
                    .build();
            startForeground(NOTIFY_ID, notification);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFY_ID);
                    InnerService.this.stopSelf();
                }
            }, 100);
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
