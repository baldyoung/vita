package com.baldyoung.vita.common.pojo.exception;

public class ExceptionBase extends Exception{
    private String desc;
    private Integer code;

    public ExceptionBase(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public ExceptionBase(String desc) {
        this.desc = desc;
    }

    public ExceptionBase() {
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
