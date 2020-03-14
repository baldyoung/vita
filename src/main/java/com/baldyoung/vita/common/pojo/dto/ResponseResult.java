package com.baldyoung.vita.common.pojo.dto;


public class ResponseResult<T> {

    public static Integer DefaultSuccessCode = 0;
    public static Integer DefaultDefeatCode = -1;
    public static Integer DefaultErrorCode = 1;

    public static String DefaultSuccessDesc = "成功";
    public static String DefaultDefeatDesc = "失败";
    public static String DefaultErrorDesc = "未知错误";


    private Integer code;
    private String desc;

    private T data;

    public static <T> ResponseResult success(T data) {
        return new ResponseResult(DefaultSuccessCode, DefaultSuccessDesc, data);
    }

    public static ResponseResult success() {
        return success(null);
    }

    public static ResponseResult defeat(String desc) {
        return new ResponseResult(DefaultDefeatCode, desc, null);
    }

    public static ResponseResult defeat() {
        return defeat(DefaultDefeatDesc);
    }

    public static ResponseResult error(Integer code, String desc) {
        return new ResponseResult(code, desc, null);
    }

    public static ResponseResult error() {
        return error(DefaultErrorCode, DefaultErrorDesc);
    }

    public ResponseResult(Integer code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
