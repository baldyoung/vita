package com.baldyoung.vita.common.pojo.enums.serviceEnums;

public enum ServiceExceptionEnum {
    // code格式：*** *** ***
    // 商品相关100100***
    PRODUCT_ID_FORMAT_WRONG("商品编号格式错误", 100100000),

    // 商品类型相关100200***
    PRODUCT_TYPE_FORMAT_WRONG("商品类型编号格式错误", 100200000),

    ;
    private String desc;
    private Integer code;
    ServiceExceptionEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }
    public String getDesc() {
        return this.desc;
    }
    public Integer getCode() {
        return this.code;
    }
}
