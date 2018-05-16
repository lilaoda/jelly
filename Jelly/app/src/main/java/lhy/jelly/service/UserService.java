package lhy.jelly.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lhy.jelly.IUserAidlInterface;
import lhy.jelly.data.local.entity.User;

/**
 * Created by Lihy on 2018/5/15 10:11
 * E-Mail ：liheyu999@163.com
 */
public class UserService extends Service {

    private User user = new User();

    private IUserAidlInterface.Stub  userBinder= new IUserAidlInterface.Stub() {
        @Override
        public void setValue(String name) throws RemoteException {
            user.setName(name);
        }

        @Override
        public String getName() throws RemoteException {
            return user.getName();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return userBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Disposable subscribe = Flowable.interval(0, 2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                user.setName("__name_" + aLong);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
        Logger.d("onCreate");
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }
}
