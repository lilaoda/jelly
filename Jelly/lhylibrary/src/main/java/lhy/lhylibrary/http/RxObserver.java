package lhy.lhylibrary.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lhy.lhylibrary.base.LhyApplication;
import lhy.lhylibrary.http.exception.ApiException;
import retrofit2.HttpException;


/**
 * Created by Liheyu on 2017/3/13.
 * Email:liheyu999@163.com
 * <p>
 * 订阅者
 */

public abstract class RxObserver<T> implements Observer<T> {

    private static String TAG = "RxObserver.class";

    private Activity mActivity;
    private ProgressDialog progressDialog;
    private Disposable mDisposable;
    private String dialogMsg;
    private boolean isUserCancel;
    private boolean isShowToast = true;

    public RxObserver() {
    }

    public RxObserver(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    public RxObserver(Activity mActivity, String dialogMsg) {
        this.mActivity = mActivity;
        this.dialogMsg = dialogMsg;
    }

    public RxObserver(boolean isShowToast,Activity mActivity, String dialogMsg) {
        this.mActivity = mActivity;
        this.isShowToast = isShowToast;
        this.dialogMsg = dialogMsg;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;
        showDialog();
    }

    @Override
    public void onNext(T value) {
        hideDialog();
        onSuccess(value);
    }

    @Override
    public void onError(final Throwable e) {
        e.printStackTrace();
        hideDialog();

        String errorMsg;
        if (e instanceof SocketTimeoutException) {
            errorMsg = "连接超时，请稍候再试";
        } else if (e instanceof ConnectException) {
            errorMsg = "连接错误，请检查网络";
        } else if (e instanceof JsonParseException) {
            errorMsg = "数据解析失败";
        }  else if (e instanceof SQLiteException) {
            errorMsg = "数据操作失败";
        }else if (e instanceof HttpException) {
            errorMsg = "连接失败";
        } else if (e instanceof IOException) {
            errorMsg = "网络错误";
        } else if (e instanceof ApiException) {
            errorMsg = e.getMessage();
        } else {
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "连接失败，请稍候再试";
        }

        //如果用户主动取消 则不提示任何信息
        if (isShowToast) {
            if (!isUserCancel) {
                Looper looper = Looper.myLooper();
                if (looper != null && looper.getThread().getId() == Looper.getMainLooper().getThread().getId()) {
                    Toast.makeText(LhyApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                isUserCancel = false;
            }
        }
        onFailure(e);
        onFailure(errorMsg);
    }

    @Override
    public void onComplete() {
        hideDialog();
    }

    private void showDialog() {
        if (mActivity == null) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setMessage(dialogMsg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.i(TAG, "user cancel");
                    isUserCancel = true;
                    mDisposable.dispose();
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

    public  abstract void onSuccess(T value);

    public void onFailure(Throwable e) {
    }

    public void onFailure(String msg) {
    }
}
