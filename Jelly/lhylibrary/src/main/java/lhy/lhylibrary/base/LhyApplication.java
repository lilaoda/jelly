package lhy.lhylibrary.base;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class LhyApplication extends Application {

    private List<LhyActivity> activitys = new LinkedList<>();
    private List<Service> services = new LinkedList<>();

    private static LhyApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        //AppCrashException.init();
    }

    public LhyActivity getCurrentActivity() {
        if (activitys.size() == 0) return null;
        return activitys.get(activitys.size() - 1);
    }


    public void addActivity(LhyActivity activity) {
        activitys.add(activity);
    }

    public void removeActivity(LhyActivity activity) {
        activitys.remove(activity);
    }

    public void closeApplication() {
        closeActivity();
        closeService();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void closeService() {
        ListIterator<Service> listIterator = services.listIterator();
        while (listIterator.hasNext()) {
            Service service = listIterator.next();
            if (service != null) {
                stopService(new Intent(this, service.getClass()));
            }
        }
    }

    public void closeActivity() {
        ListIterator<LhyActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            AppCompatActivity activity = listIterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void finishOtherActivity(LhyActivity nowAct) {
        ListIterator<LhyActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            AppCompatActivity activity = listIterator.next();
            if (activity != null && activity != nowAct) {
                activity.finish();
            }
        }
    }

    public void finishTheActivity(Class<? extends LhyActivity> nowAct) {
        ListIterator<LhyActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            AppCompatActivity activity = listIterator.next();
            if (activity != null && TextUtils.equals(activity.getClass().getName(), nowAct.getName())) {
                activity.finish();
            }
        }
    }

    public static LhyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }
}
