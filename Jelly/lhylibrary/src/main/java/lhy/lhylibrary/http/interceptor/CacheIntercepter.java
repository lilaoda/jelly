package lhy.lhylibrary.http.interceptor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import lhy.lhylibrary.utils.NetworkUtls;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 *
 * 缓存拦截器 添加缓存 只针对GET有效
 */
public class CacheIntercepter implements Interceptor {

    public static final String TAG = "CacheIntercepter";

    private Context context;

    public CacheIntercepter(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtls.isConnceted(context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtls.isConnceted(context)) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Chche-Control", "public,max-age=10")
                    .build();
            Log.d(TAG, "有网");
        } else {
            long maxage = 3600 * 24 * 4;//4天
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Chche-Control", "public,only-if-cache,max-stale=" + maxage)
                    .build();
            Log.d(TAG, "无网");
        }
        return response;
    }
}