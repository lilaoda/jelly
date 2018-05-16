package lhy.jelly.data.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {

     @POST("/user/upload")
     @FormUrlEncoded
     Observable<JsonObject> upload(@Field("name")String name,@Field("age")String age);

}
