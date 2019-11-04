package lhy.lhylibrary.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by Liheyu on 2017/4/26.
 * Email:liheyu999@163.com
 */

public class LhyFragment extends RxFragment {

    private static final String STATUS_IS_HIDDEN = "STATUS_IS_HIDDEN";

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
