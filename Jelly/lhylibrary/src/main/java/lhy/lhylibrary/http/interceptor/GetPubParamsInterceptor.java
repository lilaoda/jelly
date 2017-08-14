package lhy.lhylibrary.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 * 全部以GET方法添加公共请求信息
 */

public class GetPubParamsInterceptor implements Interceptor {

    //添加公共请求参数于集合中
    HashMap<String, String> paramsMap=new HashMap<>();

    public GetPubParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        HttpUrl oldUrl = oldRequest.url();

        HttpUrl.Builder builder = oldUrl
                .newBuilder()
                .scheme(oldUrl.scheme())
                .host(oldUrl.host());

        if (paramsMap != null) {
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpUrl newUrl = builder.build();
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(newUrl)
                .build();
        return chain.proceed(newRequest);
    }
}
