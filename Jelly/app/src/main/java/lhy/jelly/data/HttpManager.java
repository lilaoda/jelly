package lhy.jelly.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import lhy.jelly.base.Constant;
import lhy.jelly.data.remote.ApiService;
import lhy.lhylibrary.http.OkhttpManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liheyu on 2017/3/3.
 * Email:liheyu999@163.com
 */


public class HttpManager {

    private static  HttpManager INSTANCE ;
    private Retrofit mRetrofit;

    private HttpManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkhttpManager.getInstance().getCacheOKhttp())
                .build();
    }

    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }

    private Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(String.class, new TypeAdapter<String>() {

            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.value("");
                    return;
                }
                out.value(value);
            }

            @Override
            public String read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return "";
                }
                return in.nextString();
            }
        });
        return gsonBuilder.create();
    }
}
