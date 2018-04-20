package lhy.lhylibrary.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/4/21.
 * Email:liheyu999@163.com
 */

public class HeadIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original
                .newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8") // application/x-www-form-urlencoded ; charset=UTF-8
//                .addHeader("Accept-Encoding", "gzip, deflate") OKHTTP默认可以解压缩，如果加上此句，不解压缩
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json")
                .addHeader("token", "")
                .method(original.method(), original.body())
                .build();
        Response mResponse = chain.proceed(request);
        return mResponse;
    }
}
