package lhy.jelly.ui.login;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Lihy on 2018/4/26 09:43
 * E-Mail ：liheyu999@163.com
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Test
    public void testUploade(){
        Disposable subscribe = Observable.interval(0, 2, TimeUnit.SECONDS)

//                .skip(3)//跳过多少个
//                .skip(6,TimeUnit.SECONDS)//跳过多少秒
//                .take(3)//只取多少个
//                .take(6,TimeUnit.SECONDS)//只取前6秒的数据
//                .skipUntil(Observable.timer(6,TimeUnit.SECONDS))//当第二个数据源发射数据时才开始执行，以前的全部跳过
//                .skipWhile(new Predicate<Long>() {
//                    @Override
//                    public boolean test(Long aLong) throws Exception {
//                        //当函数返回第一次false的时候则开始正常发射数据，后面的不再判断
//                        return aLong<5||aLong==7;
//                    }
//                })
                .take(5)
//                .count()//统计发射了多少数据源，当所有数据源发射完成成计数并发出结果，在onnext当中只能收到结果（多少个）
//                .reduce(new BiFunction<Long, Long, Long>() {//对数据源进行计算

//                    @Override
//                    public Long apply(Long aLong, Long aLong2) throws Exception {
//                        return aLong+aLong2;
//                    }
//                })
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        return Observable.just("__"+aLong);
                    }
                }, new BiFunction<Long, String, Object>() {
                    @Override
                    public Object apply(Long aLong, String s) throws Exception {
                        return aLong+s;
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Logger.d(o);

                    }
                })
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Logger.d(aLong);
//                    }
//                })
                ;
    }

    @Before
    public void initNIM(){

    }

    @Test
    public void doLogin() {
        LoginInfo info = new LoginInfo("13922239152","KFJSALFJALFJ"); // config...
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(new RequestCallback<LoginInfo>() {

                    @Override
                    public void onSuccess(LoginInfo param) {
                        Logger.d(new Gson().toJson(param));
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }
}