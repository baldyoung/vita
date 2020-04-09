package com.baldyoung.vita.common.pojo.exception.systemException;

import com.baldyoung.vita.common.pojo.enums.EnumBase;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;

public class UtilityException extends ExceptionBase {

    public UtilityException(EnumBase enumBase) {
        this.desc = enumBase.getDesc();
        this.code = enumBase.getCode();
    }

    public UtilityException(String desc) {
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
