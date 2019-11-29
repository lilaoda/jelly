package lhy.lhylibrary.http;

import android.app.Activity;

/**
 * author: liheyu
 * date: 2019-11-06
 * email: liheyu999@163.com
 */
public abstract class HttpObserver<T> extends RxObserver<T> {

    public HttpObserver() {
    }

    public HttpObserver(boolean isShowToast) {
        super(isShowToast);
    }

    public HttpObserver(Activity mActivity, String dialogMsg) {
        super(mActivity, dialogMsg);
    }

    public HttpObserver(boolean isShowToast, Activity mActivity, String dialogMsg) {
        super(isShowToast, mActivity, dialogMsg);
    }
}
