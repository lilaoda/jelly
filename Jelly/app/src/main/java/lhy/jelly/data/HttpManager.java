package lhy.jelly.data;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import lhy.jelly.base.Constant;
import lhy.jelly.data.remote.ApiService;
import lhy.lhylibrary.http.OkHttpManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liheyu on 2017/3/3.
 * Email:liheyu999@163.com
 */


public class HttpManager {

    private static  HttpManager INSTANCE ;
    private Retrofit mRetrofit;

    private HttpManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpManager.getInstance().getCacheOKhttp())
                .build();
    }

    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }
}
