package lhy.jelly.bean;

/**
 * Created by Lihy on 2018/6/28 14:53
 * E-Mail ：liheyu999@163.com
 */
public class ApiResult<T> {

    private String msg;
    private int code; //1成功，0失败
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
