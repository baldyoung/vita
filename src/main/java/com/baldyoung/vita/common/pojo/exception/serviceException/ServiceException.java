package com.baldyoung.vita.common.pojo.exception.serviceException;

import com.baldyoung.vita.common.pojo.enums.EnumBase;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;

public class ServiceException extends ExceptionBase {

    public ServiceException(EnumBase enumBase) {
        this.desc = enumBase.getDesc();
        this.code = enumBase.getCode();
    }

    public ServiceException(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
