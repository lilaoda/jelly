package lhy.lhylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import lhy.lhylibrary.base.BaseApplication;

/**
 * Created by lilaoda on 2016/10/24.
 */
public class SPUtils {

    private static final String SP_FILE_NAME = "appConfig";

    private static SharedPreferences getSP() {
        return BaseApplication.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean putString(String key, String value) {
        boolean commit = getSP().edit().putString(key, value).commit();
        return commit;
    }

    public static String getString(String key) {
        return getSP().getString(key, "");
    }

    public static int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    public static boolean putInt(String key, int value) {
        return getSP().edit().putInt(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public static boolean putBoolean(String key, boolean value) {
        return getSP().edit().putBoolean(key, value).commit();
    }
}
