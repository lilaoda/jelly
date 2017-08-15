package lhy.jelly.ui;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import lhy.jelly.data.DbManager;
import lhy.jelly.data.remote.ApiService;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public class MainPresenter {

    private MainActivity mainActivity;
    @Inject
    ApiService apiService;
    @Inject
    DbManager dbManager;

    @Inject
    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void showToast() {
        mainActivity.showToast();
        mainActivity.showToast();
        Logger.d(apiService);
    }
}
