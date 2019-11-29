package lhy.lhylibrary.base;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;

import dagger.android.support.AndroidSupportInjection;
import lhy.lhylibrary.di.Injectable;

/**
 * Created by Liheyu on 2017/4/26.
 * Email:liheyu999@163.com
 */

public abstract class LhyFragment extends RxFragment implements Injectable {

    private static final String STATUS_IS_HIDDEN = "STATUS_IS_HIDDEN";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATUS_IS_HIDDEN);
            if (isHidden) {
                getFragmentManager().beginTransaction().hide(this).commit();
            } else {
                getFragmentManager().beginTransaction().show(this).commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATUS_IS_HIDDEN, isHidden());
    }
}
