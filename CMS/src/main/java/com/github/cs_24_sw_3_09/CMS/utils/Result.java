package com.github.cs_24_sw_3_09.CMS.utils;

public class Result<T> {
    private T value;
    private String errMsg;
    
    public Result(T value) {
        this.value = value;
        this.errMsg = null;
    }

    public Result(String errMsg) {
        this.value = null;
        this.errMsg = errMsg;
    }

    public T getValue() { return value; }
    public String getErrMsg() { return errMsg; }
    public Boolean isOk() { return value != null; }
    public Boolean isErr() { return errMsg != null; }
}
