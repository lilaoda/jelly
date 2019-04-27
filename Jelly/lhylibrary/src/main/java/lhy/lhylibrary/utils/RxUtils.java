package lhy.lhylibrary.utils;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created  on 2017/3/20 09:44
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class RxUtils {

    public static  <T> Observable<T> beginIoSubscribe(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static  <T> Observable<T> beginUiSubscribe(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
