package lhy.jelly.ui.login;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.IUserAidlInterface;
import lhy.jelly.R;
import lhy.jelly.data.remote.ApiService;
import lhy.lhylibrary.base.LhyActivity;
import lhy.lhylibrary.http.RxObserver;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends LhyActivity  {

    private static final int CODE_REQUEST_IMG = 10;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;
    private ArrayList<String> mImgList = new ArrayList<>();
    private IUserAidlInterface iUserAidlInterface;
    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        testUpload();
//        bindService(new Intent(this,UserService.class),conn, Context.BIND_AUTO_CREATE);
//        startActivity(new Intent(this, MainActivity.class));
//        DownloadManager downloadManager = new DownloadManager(this);
//        downloadManager.updateAPP("http://192.168.8.62:8080/imageLoad/pda.apk", FileUtils.getSDCardPath()+"/jelly/");
//
//    openAlbum();
    }

    private void testUpload(){
        apiService.upload("android","18").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject value) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (iUserAidlInterface == null) {
                iUserAidlInterface = IUserAidlInterface.Stub.asInterface(service);
            }

            if (iUserAidlInterface != null) {
                try {
                    iUserAidlInterface.setValue("fsdffffffffff");
                    String name1 = iUserAidlInterface.getName();
                    Logger.d("logind" + name1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("onServiceDisconnected");
        }
    };

}
