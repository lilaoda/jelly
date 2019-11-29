package lhy.jelly.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import lhy.jelly.R;
import lhy.jelly.base.BaseActivity;
import lhy.jelly.base.Constant;
import lhy.jelly.bean.ApiResult;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.data.local.gen.UserDao;
import lhy.jelly.data.remote.ApiService;
import lhy.jelly.util.RxUtils;
import lhy.lhylibrary.http.RxObserver;
import lhy.lhylibrary.utils.CommonUtils;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;

    @Inject
    ApiService apiService;
    @Inject
    UserDao userDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        editAccount.setText("13922239152");
        editPassword.setText("123456");
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (checkData()) {
//            doLogin();
            gotoMain();
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
        RxUtils.wrapHttp(login)
                .compose(this.<User>bindToLifecycle())
                .subscribe(new RxObserver<User>(true, this, "正在登陆...") {
                    @Override
                    public void onSuccess(User value) {
                        gotoMain();
                    }
                });
    }



    private void gotoMain() {
        ARouter.getInstance().build(Constant.ROUTE_PATH_JELLY_MAIN_ACTIVITY).navigation();
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }
}
