package com.xjx.commons.domain;

public class ReturnObject {
    private Integer code;
    private String message;
    private Object rtn;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRtn() {
        return rtn;
    }

    public void setRtn(Object rtn) {
        this.rtn = rtn;
    }
}
