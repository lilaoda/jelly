package lhy.lhylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import lhy.lhylibrary.base.LhyApplication;


public class SPUtils {

    private static final String SP_FILE_NAME = "appConfig";

    public static SharedPreferences getSP() {
        return LhyApplication.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean putString(String key, String value) {
        return getSP().edit().putString(key, value).commit();
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
