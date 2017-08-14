package lhy.lhylibrary.http.interceptor;


import io.reactivex.functions.Function;
import lhy.lhylibrary.http.exception.ApiException;
import lhy.lhylibrary.http.HttpResult;

/**
 * Created by Liheyu on 2017/3/9.
 * Email:liheyu999@163.com
 * APP数据接口异常处理
 */

public class ExceptionFunction<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {

        //如果请求数据不成功，就抛异常走onError返回服务器返回的信息
        if (!tHttpResult.isSuccess()) {
            throw new ApiException(tHttpResult.getReason());
        }

        return tHttpResult.getData();
    }
}
