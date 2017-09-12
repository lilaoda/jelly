package lhy.lhylibrary.http.exception;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import lhy.lhylibrary.utils.DateUtils;
import lhy.lhylibrary.utils.FileUtils;

/**
 * Created by Liheyu on 2016/5/15.
 * Email:liheyu999@163.com
 * APP crash 错误日志收集
 */

public class AppCrashException implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "AppCrashException";
    public static final String DIR_CRASH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LhyLibrary";
    private static AppCrashException INSTANCE = new AppCrashException();

    //must init in aplliction  otherwise crash;
    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        ex.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                writeString2File(getStringException(ex));
                e.onNext("");
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe();
    }

    private String getStringException(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.close();
        String result = stringWriter.toString();
        writeString2File(result);
        return result;
    }

    private void writeString2File(String content) {
        if (FileUtils.isSDCardEnabled()) {
            File file = new File(DIR_CRASH, DateUtils.getCurrentTime());
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
