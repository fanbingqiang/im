package com.it.live.entity;

public class WebResDTO {
    public static final int SUCCESS = 200;

    public static final int FAIL = 500;

    private int code = 0;

    private Object data;

    @Override
    public String toString() {
        return "WebResDTO{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

    public WebResDTO(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static WebResDTO success(Object data) {
        return new WebResDTO(SUCCESS, data);
    }

    public static WebResDTO success(String message) {
        return new WebResDTO(SUCCESS, message);
    }

    public static WebResDTO success(long data) {
        return new WebResDTO(SUCCESS, data);
    }

    public static WebResDTO fail(String message) {
        return new WebResDTO(FAIL, message);
    }

    public static WebResDTO error(String message) {
        return new WebResDTO(FAIL, message);
    }
}
