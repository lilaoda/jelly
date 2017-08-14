package lhy.lhylibrary.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 * 添加公共请求头
 */

public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("User-Agent", "Android, xxx")
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
