package com.baldyoung.vita.common.pojo.enums.systemEnums;

import com.baldyoung.vita.common.pojo.enums.EnumBase;

public enum SystemExceptionEnums implements EnumBase {
    ROOM_ID_NOT_EXISTS("餐位编号不存在", 200100100),

    ;

    private String desc;
    private Integer code;

    SystemExceptionEnums(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
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
