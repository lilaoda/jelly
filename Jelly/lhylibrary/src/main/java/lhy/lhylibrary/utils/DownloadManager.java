package lhy.lhylibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import lhy.lhylibrary.R;
import lhy.lhylibrary.base.LhyApplication;

/**
 * Created by Liheyu on 2017/9/25.
 * Email:liheyu999@163.com
 * 下载APP,并通知安装
 */

public class DownloadManager {

    public static final String TAG = "DownloadManager";
    private static final int NOTIFY_ID = 9;

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotifyBuilder;
    private Dialog progressDialog;
    private Activity mActivity;


    public DownloadManager(Context context) {
        this.mContext = context;
        FileDownloader.setup(context);
        initNotification();
    }

    public void updateAPP(String url, String directoryPath) {
        String direcotryPath = Environment.getExternalStorageDirectory().getPath() + "/" + "test/";
        File file = new File(direcotryPath);
        if (!file.exists()) file.mkdirs();
        FileDownloader.getImpl().create(url)
                .setPath(directoryPath, true)
                .setForceReDownload(true)
                .setCallbackProgressTimes(100)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "url:"+task.getUrl()+"\n"+"fileName:"+task.getFilename());
                        Log.d(TAG, "pending: "+"大小："+FileUtils.getFormatSize(totalBytes));
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "connected: "+totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "progress: "+soFarBytes +"总大小："+totalBytes);
                        Notification notification = mNotifyBuilder.setProgress(totalBytes, soFarBytes, false)
                                .setContentInfo(FileUtils.getFormatSize(soFarBytes)+"/"+ soFarBytes/(float)totalBytes*10000/100+"%")
                                .build();
                        mNotificationManager.notify(NOTIFY_ID, notification);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.d(TAG, "blockComplete: ");
                    }

                    @Override

                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed: ");
                        notifyInstallAPK(task.getTargetFilePath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e(TAG, "error: ");
                        e.printStackTrace();
                        File tempFile = new File(task.getTargetFilePath());
                        if (tempFile.exists()) tempFile.delete();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    private void notifyInstallAPK(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setContentText("点击安装")
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotifyBuilder.build());
    }

    private void installApp(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //初始化通知
    private void initNotification() {
        mNotifyBuilder = new NotificationCompat.Builder(mContext);
        mNotifyBuilder.setContentTitle(mContext.getResources().getString(R.string.app_name) + "升级")
                .setSmallIcon(R.mipmap.loading)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mis_asv))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true);
        mNotificationManager = (NotificationManager) LhyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void showDialog() {
        //如果未传activity就不显示对话框
        if (mActivity == null) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mActivity,ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
