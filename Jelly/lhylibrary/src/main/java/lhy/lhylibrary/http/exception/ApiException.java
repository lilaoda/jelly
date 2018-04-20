package lhy.lhylibrary.http.exception;



/**
 * Created by Liheyu on 2017/3/8.
 * Email:liheyu999@163.com
 */

public class ApiException extends Exception {


    public ApiException() {

    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

}
