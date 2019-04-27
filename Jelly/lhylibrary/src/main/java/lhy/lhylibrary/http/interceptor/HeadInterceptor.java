package lhy.lhylibrary.http.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/4/21.
 * Email:liheyu999@163.com
 * 表单：application/x-www-form-urlencoded ; charset=UTF-8
 * JSON：application/json; charset=UTF-8
 * .addHeader("Accept-Encoding", "gzip, deflate") OKHTTP 默认可以解压缩，如果加上此句，不解压缩
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original
                .newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json")
                .addHeader("token", "")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
