package lhy.jelly.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lhy.jelly.R;
import lhy.lhylibrary.base.BaseActivity;

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
    }
}
