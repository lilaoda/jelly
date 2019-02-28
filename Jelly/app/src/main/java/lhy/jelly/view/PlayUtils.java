package lhy.jelly.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created  on 2018/8/2 10:09
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class PlayUtils {
    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity)context;
        } else {
            return context instanceof ContextWrapper ? scanForActivity(((ContextWrapper)context).getBaseContext()) : null;
        }
    }

    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof AppCompatActivity) {
            return (AppCompatActivity)context;
        } else {
            return context instanceof ContextThemeWrapper ? getAppCompActivity(((ContextThemeWrapper)context).getBaseContext()) : null;
        }
    }

    public static void showActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
//            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }

        scanForActivity(context).getWindow().clearFlags(1024);
    }

    public static void hideActionBar(Context context) {
        ActionBar ab = getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
//            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }

        scanForActivity(context).getWindow().setFlags(1024, 1024);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal, context.getResources().getDisplayMetrics());
    }

    public static String formatTime(long milliseconds) {
        if (milliseconds > 0L && milliseconds < 86400000L) {
            long totalSeconds = milliseconds / 1000L;
            long seconds = totalSeconds % 60L;
            long minutes = totalSeconds / 60L % 60L;
            long hours = totalSeconds / 3600L;
            StringBuilder stringBuilder = new StringBuilder();
            Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
            return hours > 0L ? mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString() : mFormatter.format("%02d:%02d", minutes, seconds).toString();
        } else {
            return "00:00";
        }
    }

    public static void savePlayPosition(Context context, String url, long position) {
        context.getSharedPreferences("NICE_VIDEO_PALYER_PLAY_POSITION", 0).edit().putLong(url, position).apply();
    }

    public static long getSavedPlayPosition(Context context, String url) {
        return context.getSharedPreferences("NICE_VIDEO_PALYER_PLAY_POSITION", 0).getLong(url, 0L);
    }
}
