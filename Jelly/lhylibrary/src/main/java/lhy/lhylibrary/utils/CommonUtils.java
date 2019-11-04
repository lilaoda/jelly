package lhy.lhylibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lhy.lhylibrary.base.LhyApplication;


public class CommonUtils {

    private CommonUtils() {

    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }


    public static int dp2px(Context context, int dp) {
        float density = getResources(context).getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * px too dp
     *
     * @param px
     * @return
     */
    public static int px2dp(Context context, int px) {
        float density = getResources(context).getDisplayMetrics().density;
        return (int) (px * density + 0.5f);
    }


    /**
     * 移除view的父容器
     *
     * @param view
     */
    public static void removerParent(View view) {
        ViewParent parent = view.getParent();
        //当fragment销毁后通过反射调用无参construction创建新的fragment实例时，parent为空
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    public static String getStringfromStream(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        try {
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    public static String getString(TextView v) {
        return v.getText().toString().trim();
    }

    public static String getString(@StringRes int id) {
        return getResources(LhyApplication.getContext()).getString(id);
    }


    /**
     * 重新计算Listview高度，解决scrollview嵌套冲突
     *
     * @param listView
     */
    public static void setListViewHeightByChild(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void installAPK(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void doCall(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeigth(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
