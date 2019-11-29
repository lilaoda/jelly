package lhy.jelly.base;


import androidx.annotation.NonNull;

/**
 * author: liheyu
 * date: 2019-11-06
 * email: liheyu999@163.com
 */
public class Resource<T> {

    private Status status;
    private T data;
    private String msg;

    public Resource(Status status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(@NonNull T t) {
        return new Resource<>(Status.SUCCESS, t, null);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
