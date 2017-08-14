package lhy.lhylibrary.utils;

import android.view.Gravity;
import android.widget.Toast;

import lhy.lhylibrary.base.BaseApplication;


public class ToastUtils {

    private ToastUtils() {
    }

    public static void showString(String content) {
        Toast toast = Toast.makeText(BaseApplication.getContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,120);
        toast.show();
    }

    public static void showInt(int resId) {
        Toast.makeText(BaseApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showNetError() {
        Toast.makeText(BaseApplication.getContext(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
    }

    public static void showNomore() {
        Toast.makeText(BaseApplication.getContext(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
    }
}
