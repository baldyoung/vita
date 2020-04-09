package com.baldyoung.vita.common.pojo.enums.systemEnums;

import com.baldyoung.vita.common.pojo.enums.EnumBase;

public class SystemExceptionEnums implements EnumBase {

    private String desc;
    private Integer code;

    public SystemExceptionEnums(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Integer getCode() {
        return null;
    }
}
