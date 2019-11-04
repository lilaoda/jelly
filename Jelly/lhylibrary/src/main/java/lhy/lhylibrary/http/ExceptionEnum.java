package lhy.lhylibrary.http;

/**
 * Created by Lihy on 2018/5/14 14:13
 * E-Mail ：liheyu999@163.com
 */
public enum ExceptionEnum {
    TOKEN_ERROL(401, "token失效");

    private int code;
    private String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ExceptionEnum formCode(int code) {
        ExceptionEnum[] values = ExceptionEnum.values();
        for (ExceptionEnum value : values) {
            if (value.code == code)
                return value;
        }
        throw new IllegalArgumentException("无效的code");
    }
}
