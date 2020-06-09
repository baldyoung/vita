package com.baldyoung.vita.common.pojo.exception.systemException;

import com.baldyoung.vita.common.pojo.enums.EnumBase;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;

public class UtilityException extends ExceptionBase {

    public UtilityException(EnumBase enumBase) {
        super.desc = enumBase.getDesc();
        super.code = enumBase.getCode();
    }

    public UtilityException(String desc) {
        super.desc = desc;
    }

    @Override
    public String getDesc() {
        return super.desc;
    }

    @Override
    public Integer getCode() {
        return super.code;
    }
}
