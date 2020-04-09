package com.baldyoung.vita.common.pojo.exception;

import com.baldyoung.vita.common.pojo.enums.EnumBase;

public abstract class ExceptionBase extends Exception{
    protected String desc;

    protected Integer code;

    public abstract String getDesc();

    public abstract Integer getCode();




}
