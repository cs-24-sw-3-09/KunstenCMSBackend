package com.github.cs_24_sw_3_09.CMS.utils;

public class Result<T, E> {
    private T value;
    private E error;
    
    public Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(value, null); 
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(null, error); 
    }

    public T getOk() { return value; }
    public E getErr() {return error; }
    public Boolean isOk() { return value != null; }
    public Boolean isErr() { return error != null; }
}