package lhy.jelly.ui.login;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import lhy.jelly.IUserAidlInterface;
import lhy.jelly.R;
import lhy.jelly.base.AbstractDiActivity;
import lhy.jelly.bean.ApiResult;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.data.local.gen.UserDao;
import lhy.jelly.data.remote.ApiService;
import lhy.jelly.service.UserService;
import lhy.jelly.ui.main.MainActivity;
import lhy.jelly.util.RxUtils;
import lhy.lhylibrary.http.RxObserver;
import lhy.lhylibrary.http.exception.ApiException;
import lhy.lhylibrary.utils.CommonUtils;
import lhy.lhylibrary.utils.SPUtils;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends AbstractDiActivity {

    private static final int CODE_REQUEST_IMG = 10;

    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;

    private ArrayList<String> mImgList = new ArrayList<>();
    private IUserAidlInterface iUserAidlInterface;
    @Inject
    ApiService apiService;
    @Inject
    UserDao userDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        bindService(new Intent(this, UserService.class), conn, Context.BIND_AUTO_CREATE);
        initView();
    }

    private void initView() {
        String account = SPUtils.getString("account");
        String token = SPUtils.getString("token");
//        editAccount.setText(account);
        editAccount.setText("13922239152");
        editPassword.setText("123456");
//        if (!TextUtils.isEmpty(account)) {
//            gotoMain();
//        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (checkData()) {
            doLogin();
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(CommonUtils.getString(editAccount)) || TextUtils.isEmpty(CommonUtils.getString(editPassword))) {
            ToastUtils.showString("账号或密码不能为空");
            return false;
        }
        return true;
    }

    public void doLogin() {
        User user = new User();
        user.setAccount(CommonUtils.getString(editAccount));
        user.setPassword(CommonUtils.getString(editPassword));
        Observable<ApiResult<User>> login = apiService.login(user);
        RxUtils.wrapHttp(login).flatMap(new Function<User, ObservableSource<LoginInfo>>() {
            @Override
            public ObservableSource<LoginInfo> apply(User user) throws Exception {
                userDao.insertOrReplace(user);
                return loginImObservable(new LoginInfo(user.getAccount(), user.getToken()));
            }
        })
                .compose(this.<LoginInfo>bindToLifecycle())
                .subscribe(new RxObserver<LoginInfo>(true, this, "正在登陆...") {
                    @Override
                    public void onSuccess(LoginInfo value) {
                        gotoMain();
                    }
                });
    }

    /**
     * 登陆IM
     * @param info 账号信息
     */
    private Observable<LoginInfo> loginImObservable(final LoginInfo info) {
        return Observable.create(new ObservableOnSubscribe<LoginInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<LoginInfo> e) throws Exception {
                NIMClient.getService(AuthService.class).login(info)
                        .setCallback(new RequestCallback<LoginInfo>() {
                            @Override
                            public void onSuccess(LoginInfo param) {
//                        {"account":"13922239152","token":"cfc78aee2a8e432ecd9447e997146234"}
                                //  Preferences.
                                SPUtils.putString("account", param.getAccount());
                                SPUtils.putString("token", param.getToken());
                                NimUIKit.loginSuccess(param.getAccount());
                                NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
                                    @Override
                                    public boolean shouldIgnore(IMMessage message) {
                                        return false;
                                    }
                                });
                                NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
                                    @Override
                                    public boolean shouldIgnore(IMMessage message) {
                                        return false;
                                    }
                                });
                                e.onNext(param);
                                e.onComplete();
                            }

                            @Override
                            public void onFailed(int code) {
                                e.onError(new ApiException(code + ""));
                                e.onComplete();
                            }

                            @Override
                            public void onException(Throwable exception) {
                                e.onError(exception);
                                e.onComplete();
                            }
                        });
            }
        });
    }


    private void gotoMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        super.onPause();
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
