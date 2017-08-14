package lhy.lhylibrary.http;

/**
 * Created by Liheyu on 2017/3/6.
 * Email:liheyu999@163.com
 */

public class HttpResult<T> {
    private T data;
    private boolean success;
    private String reason;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
