package lhy.jelly.ui.login;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.IUserAidlInterface;
import lhy.jelly.R;
import lhy.jelly.data.remote.ApiService;
import lhy.jelly.service.UserService;
import lhy.jelly.ui.main.MainActivity;
import lhy.lhylibrary.base.LhyActivity;
import lhy.lhylibrary.http.RxObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends LhyActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        bindService(new Intent(this,UserService.class),conn, Context.BIND_AUTO_CREATE);
       // setupWindowAnimations();
    }

    @TargetApi(value = 21)
    private void setupWindowAnimations() {
        Transition fadeTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        Fade fade = new Fade();
        getWindow().setExitTransition(fadeTransition);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
//        testRxjava();
//        testUpload();

        gotoMain();
//        DownloadManager downloadManager = new DownloadManager(this);
//        downloadManager.updateAPP("http://192.168.8.62:8080/imageLoad/pda.apk", FileUtils.getSDCardPath()+"/jelly/");
//
//    openAlbum();
    }

   // @TargetApi(value = 21)
    private void gotoMain() {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//        ActivityCompat.startActivity(this,new Intent(this, MainActivity.class),activityOptions.toBundle());
        startActivity(new Intent(this, MainActivity.class),activityOptions.toBundle());
//        startActivity(new Intent(this, MainActivity.class));
    }

    private void testRxjava() {
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
//                .take(5)
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

    private void testUpload() {
        List<String> list = new ArrayList<>();
        String descriptionString = "This is a description";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        apiService.upload("android", "18",description,getMultipartBody()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject value) {

                    }
                });
    }

    public  MultipartBody.Part getMultipartBody() {
        final MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File("/storage/emulated/0/83009038.jpg");
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
        MultipartBody.Part img = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        MultipartBody.Part part = MultipartBody.Part.create(requestBody);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        String des = "photo" + 1;
        builder.addFormDataPart(des, file.getName(), requestBody);
      //  builder.setType(MultipartBody.FORM);
        return img;
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
