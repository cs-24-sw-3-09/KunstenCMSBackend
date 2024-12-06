package com.github.cs_24_sw_3_09.CMS.model.entities;

public class Result<T> {
    private T value;
    private String errorMsg;
    
    public Result(T value) {
        this.value = value; 
        this.errorMsg = null;
    }
    
    public Result(String errorMsg) {
        this.value = null; 
        this.errorMsg = errorMsg;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return errorMsg;
    }

    public Boolean isOk() {
        return errorMsg == null;
    }

    public Boolean isError() {
        return value == null;
    }

}
