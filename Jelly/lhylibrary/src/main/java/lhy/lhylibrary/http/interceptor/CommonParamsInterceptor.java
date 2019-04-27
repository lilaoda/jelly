package lhy.lhylibrary.http.interceptor;

import java.io.IOException;

import lhy.lhylibrary.BuildConfig;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 *
 * 根据方法自动添加参数的公共请求头
 */

public class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET")) {
            HttpUrl httpUrl = request.url().newBuilder()
                    .addQueryParameter("version", BuildConfig.VERSION_NAME)
                    .addQueryParameter("device", "Android")
                    .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();
            request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();

                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }

                formBody = bodyBuilder
                        .addEncoded("version", BuildConfig.VERSION_NAME)
                        .addEncoded("device", "Android")
                        .addEncoded("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build();

                request = request.newBuilder().post(formBody).build();
            }
        }

        return chain.proceed(request);
    }
}
