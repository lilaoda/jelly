package lhy.jelly.data.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {
     String BASE_URL= "http://192.168.8.59:8080/";

     @GET("hello")
     Observable<JsonObject> getHello();

}
