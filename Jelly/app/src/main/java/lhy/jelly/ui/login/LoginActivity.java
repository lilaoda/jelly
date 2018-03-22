package lhy.jelly.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.data.HttpManager;
import lhy.lhylibrary.base.BaseActivity;
import lhy.lhylibrary.http.HttpObserver;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        HttpManager.getInstance().getApiService().getHello().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<JsonObject>bindToLifecycle())
                .subscribe(new HttpObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject value) {

                    }
                });
    }
}
