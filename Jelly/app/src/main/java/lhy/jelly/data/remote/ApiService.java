package lhy.jelly.data.remote;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import lhy.jelly.bean.ApiResult;
import lhy.jelly.data.local.entity.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {

     /**
      * 上传图片
      */
     @POST("/user/upload")
     @Multipart
     Observable<JsonObject> upload(@Query("name") String name, @Query("age")String age, @Part("description") RequestBody body1, @Part MultipartBody.Part body);

     @POST("/user/login")
     Observable<ApiResult<User>> login(@Body User user);

}
