package lhy.lhylibrary.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 * 全部以POST方式添加公共请求信息
 */

public class PostPubParamsInterceptor implements Interceptor {

    //公共请求参数添加于此
    HashMap<String, String> paramsMap;

    public PostPubParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orgRequest = chain.request();
        RequestBody body = orgRequest.body();

        if (body != null) {
            RequestBody newBody = null;
            if (body instanceof FormBody) {
                newBody = addParamsToFormBody((FormBody) body);
            } else if (body instanceof MultipartBody) {
                newBody = addParamsToMultipartBody((MultipartBody) body);
            }

            if (null != newBody) {
                Request newRequest = orgRequest.newBuilder()
                        .url(orgRequest.url())
                        .method(orgRequest.method(), newBody)
                        .build();

                return chain.proceed(newRequest);
            }
        }
        return chain.proceed(orgRequest);
    }

    // 为MultipartBody类型请求体添加参数 paramsBuilder打印用
    private MultipartBody addParamsToMultipartBody(MultipartBody body) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (paramsMap != null) {
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addPart(body.part(i));
        }
        return builder.build();
    }

    /**
     * 为FormBody类型请求体添加参数
     */
    private FormBody addParamsToFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();

        if (paramsMap != null) {
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }
        return builder.build();
    }
}
