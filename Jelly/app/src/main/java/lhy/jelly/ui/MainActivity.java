package lhy.jelly.ui;

import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import lhy.jelly.JellyApplicaiton;
import lhy.jelly.R;
import lhy.lhylibrary.base.BaseActivity;
import lhy.lhylibrary.utils.ToastUtils;

public class MainActivity extends BaseActivity {

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.showToast();
            }
        });
        DaggerMainComponent.builder()
                .applicationComponent(((JellyApplicaiton) getApplication()).getApplicationComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    public void showToast() {
        ToastUtils.showString("abcd");
    }
}
