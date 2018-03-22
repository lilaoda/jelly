package lhy.lhylibrary.http;


import com.ihsanbal.logging.LoggingInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lhy.lhylibrary.base.LhyApplication;
import lhy.lhylibrary.http.interceptor.CacheIntercepter;
import lhy.lhylibrary.http.interceptor.HeadIntercepter;
import lhy.lhylibrary.utils.FileUtils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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


    public OkHttpClient getOKhttp() {
        return mOkHttpBuilder.build();
    }

    public OkHttpClient getCacheOKhttp() {
        return mOkHttpBuilder
                .cache(new Cache(FileUtils.getCacheFile(LhyApplication.getContext(), "file_cache"), 1024 * 1024 * 100))
                .addInterceptor(new CacheIntercepter(LhyApplication.getContext()))
                .build();
    }

    //提交单文件表单示例
    public void oneFormFileSample(String imgPath) {
        File file = new File(imgPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        String descriptionString = "This is a description";
    }


    public MultipartBody getMultipartBody(List<String> imgPaths) {
        final MultipartBody.Builder builder = new MultipartBody.Builder();
        if (imgPaths != null && imgPaths.size() > 0) {
            for (int i = 0; i < imgPaths.size(); i++) {
                File file = new File(imgPaths.get(i));
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                String des = "photo" + i + 1;
                builder.addFormDataPart(des, file.getName(), requestBody);
            }
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    public List<MultipartBody.Part> getMultipartBodyPartList(List<String> imgPaths) {
        List<MultipartBody.Part> list = new ArrayList<>();
        if (imgPaths != null && imgPaths.size() > 0) {
            for (int i = 0; i < imgPaths.size(); i++) {
                File file = new File(imgPaths.get(i));
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                String des = "aFile";
                MultipartBody.Part part = MultipartBody.Part.createFormData(des, file.getName(), requestBody);
                list.add(part);
            }
        }
        return list;
    }
}