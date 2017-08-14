package lhy.lhylibrary.http;


import com.ihsanbal.logging.LoggingInterceptor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import lhy.lhylibrary.base.BaseApplication;
import lhy.lhylibrary.http.interceptor.CacheIntercepter;
import lhy.lhylibrary.http.interceptor.HeadIntercepter;
import lhy.lhylibrary.utils.FileUtils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

/**
 * OkHttp管理类，可添加缓存，添加公共请求参数
 */
public class OkhttpManager {

    private static OkhttpManager instance;
    private final OkHttpClient.Builder mOkHttpBuilder;

    private static final int CONNECTIMEOUT = 10000;
    private static final int READTIMEOUT = 10000;

    private OkhttpManager() {
        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeadIntercepter())
                .addInterceptor(new LoggingInterceptor.Builder()
                        .loggable(true)
//                        .loggable(BuildConfig.DEBUG)
                        .setLevel(com.ihsanbal.logging.Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .addHeader("version", android.support.graphics.drawable.animated.BuildConfig.VERSION_NAME)
                        .build())
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));//明文Http与比较新的Https
    }

    public static OkhttpManager getInstance() {
        if (instance == null) {
            instance = new OkhttpManager();
        }
        return instance;
    }

    /**
     * 不带缓存的Okhttp客户端
     *
     * @return
     */
    public OkHttpClient getOKhttp() {
        return mOkHttpBuilder.build();
    }

    /**
     * 带缓存的OKhttp客户羰
     *
     * @return
     */
    public OkHttpClient getCacheOKhttp() {
        return mOkHttpBuilder
                .cache(new Cache(FileUtils.getCacheFile(BaseApplication.getContext(), "file_cache"), 1024 * 1024 * 100))
                .addInterceptor(new CacheIntercepter(BaseApplication.getContext()))
                .build();
    }
}