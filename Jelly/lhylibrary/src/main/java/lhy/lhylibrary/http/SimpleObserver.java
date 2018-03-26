package lhy.lhylibrary.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lhy.lhylibrary.base.LhyApplication;
import lhy.lhylibrary.http.exception.ApiException;
import retrofit2.HttpException;


public abstract class SimpleObserver<T> implements Observer<T> {

    private  static  String TAG ="SimpleObserver.class";

    private Activity mActivity;
    private ProgressDialog progressDialog;
    private Disposable mDisposable;
    private String dialogMsg;
    private boolean isUserCancel ;
    private boolean isShowToast;
    private boolean isShowDialog;

    public SimpleObserver() {
    }

    public SimpleObserver(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    public SimpleObserver(Activity mActivity, boolean isShowDialog, boolean isShowToast) {
        this.mActivity = mActivity;
        this.isShowToast = isShowToast;
        this.isShowDialog = isShowDialog;
    }

    public SimpleObserver(Activity mActivity, boolean isShowDialog, String dialogMsg, boolean isShowToast) {
        this.mActivity = mActivity;
        this.isShowToast = isShowToast;
        this.isShowDialog = isShowDialog;
        this.dialogMsg = dialogMsg;
    }

    public SimpleObserver(Activity mActivity, String dialogMsg, boolean isShowToast) {
        this.mActivity = mActivity;
        this.isShowToast = isShowToast;
        this.isShowDialog = true;
        this.dialogMsg = dialogMsg;
    }

    public SimpleObserver(Activity mActivity, String dialogMsg) {
        this.mActivity = mActivity;
        this.isShowDialog = true;
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
        if (e instanceof IOException) {
            errorMsg = "网络错误";
        } else if (e instanceof HttpException) {
            errorMsg = "网络错误";
        } else if (e instanceof TimeoutException) {
            errorMsg = "连接超时，请稍候再试";
        } else if (e instanceof ApiException) {
            errorMsg = e.getMessage();
        } else {
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "未知错误";
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
        //如果未传activity就不显示对话框
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
                    //用户主动取消时调用
                    Log.i(TAG, "cancel");
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

    public abstract void onSuccess(T value);

    public void onFailure(Throwable e) {
    }

    public void onFailure(String msg) {
    }
}
