package lhy.jelly.util;


import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.base.Resource;
import lhy.jelly.bean.ApiResult;
import lhy.lhylibrary.http.HttpObserver;
import lhy.lhylibrary.http.exception.ApiException;

/**
 * Created by Lihy on 2018/6/28 14:55
 * E-Mail ：liheyu999@163.com
 */
public class RxUtils {

    public static <T> Observable<T> wrapHttp(Observable<ApiResult<T>> observable) {
        return observable.map(new Function<ApiResult<T>, T>() {
            @Override
            public T apply(ApiResult<T> tApiResult) throws Exception {
                if (tApiResult.getCode() != 1) {
                    throw new ApiException(tApiResult.getMsg());
                }
                return tApiResult.getData();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> wrapRx(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> void wrapLiveData(Observable<T> observable, MutableLiveData<Resource<T>> liveData) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> liveData.setValue(Resource.loading()))
                .subscribe(new HttpObserver<T>() {
                    @Override
                    public void onSuccess(T value) {
                        if(liveData.hasActiveObservers())
                        liveData.setValue(Resource.success(value));
                    }

                    @Override
                    public void onFailure(String msg) {
                        liveData.setValue(Resource.error(msg));
                    }
                });
    }
}
