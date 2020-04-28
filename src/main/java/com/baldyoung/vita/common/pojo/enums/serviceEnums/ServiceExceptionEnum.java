package com.baldyoung.vita.common.pojo.enums.serviceEnums;

import com.baldyoung.vita.common.pojo.enums.EnumBase;

public enum ServiceExceptionEnum implements EnumBase {
    // code格式：*** *** ***
    // 商品相关100100***
    PRODUCT_ID_FORMAT_WRONG("商品编号格式错误", 100100000),
    PRODUCT_NAME_EXISTS("商品名称已存在", 100100001),

    // 商品类型相关100200***
    PRODUCT_TYPE_FORMAT_WRONG("商品类型编号格式错误", 100200000),
    PRODUCT_TYPE_NAME_EXISTS("名称已存在", 100200001),

    // 订单相关100300***

    // 购物车相关100400***
    SHOPPING_CART_PRE_ORDER("购物车下单中，不允许修改商品", 100400100)

    ;
    private String desc;
    private Integer code;
    ServiceExceptionEnum(String desc, Integer code) {
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
