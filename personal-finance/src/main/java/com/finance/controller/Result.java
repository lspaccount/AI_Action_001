package com.finance.controller;

public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result success(String message) {
        return new Result(200, message, null);
    }

    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    public static Result error(String message) {
        return new Result(500, message, null);
    }
}