package lhy.lhylibrary.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 */

public class LoggingInterceptor implements Interceptor {

    public static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.e("request:", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers(), request.body().toString()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        if (response != null) {
            Log.e("response:", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        }
        return response;
    }
}
