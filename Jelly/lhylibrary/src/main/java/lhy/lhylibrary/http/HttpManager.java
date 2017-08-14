package lhy.lhylibrary.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liheyu on 2017/3/3.
 * Email:liheyu999@163.com
 */

public class HttpManager {

    private static HttpManager instance = null;

    private Retrofit.Builder mRetrofitBuilder;

    private HttpManager() {
        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkhttpManager.getInstance().getCacheOKhttp());
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public Retrofit.Builder getRetrofit() {
        return mRetrofitBuilder;
    }
}
