package lhy.jelly.util;

/**
 * author: liheyu
 * date: 2019-11-05
 * email: liheyu999@163.com
 */
public class Event<T> {

    private T t;
    private boolean loading;
    private String errorMsg;

    public Event(T t, boolean loading, String errorMsg) {
        this.t = t;
        this.loading = loading;
        this.errorMsg = errorMsg;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
