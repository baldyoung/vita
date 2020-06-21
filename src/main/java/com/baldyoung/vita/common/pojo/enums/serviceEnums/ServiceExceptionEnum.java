package com.baldyoung.vita.common.pojo.enums.serviceEnums;

import com.baldyoung.vita.common.pojo.enums.EnumBase;

public enum ServiceExceptionEnum implements EnumBase {
    // code格式：*** *** ***
    // 商品相关100100***
    PRODUCT_ID_FORMAT_WRONG("商品编号格式错误", 100100100),
    PRODUCT_NAME_EXISTS("商品名称已存在", 100100101),
    PRODUCT_NOT_FOUND("商品不存在", 100100102),
    PRODUCT_ID_IS_NULL("商品编号为空", 100100103),
    PRODUCT_UNDERSTOCK("商品库存不足", 100100104),

    // 商品类型相关100200***
    PRODUCT_TYPE_FORMAT_WRONG("商品类型编号格式错误", 100200000),
    PRODUCT_TYPE_NAME_EXISTS("名称已存在", 100200001),

    // 订单相关100300***
    ORDER_IS_EMPTY("订单为空", 100300100),
    NO_ORDER("无订单数据", 100300101),


    // 购物车相关100400***
    SHOPPING_CART_PRE_ORDER("购物车下单中，不允许修改商品", 100400100),
    SHOPPING_CART_NOT_FOUND("非法购物车", 100400101),
    SHOPPING_CART_NO_POWER("无权限操作", 100400102),
    SHOPPING_CART_TIME_OUT("权限超时", 100400103),
    SHOPPING_CART_EMPTY("购物车为空", 100400104),
    SHOPPING_SUBMIT_EMPTY("提交内容为空", 100400105),

    // 系统消息100500***
    MESSAGE_NO_FOUND("该消息不存在", 100500100),

    // 账单相关100600***
    BILL_NO_FOUND("无账单", 100600100),

    // 就餐位相关100700***
    ROOM_NOT_FOUND("该就餐位不存在", 100700100),

    // 商家用户相关100800***
    MERCHANT_GRADE_FORBIDDEN("账号权限不足", 100800100),
    MERCHANT_LOGIN_FILED("账号或密码错误", 100800101),
    MERCHANT_ACCOUNT_EXISTS("账号名已存在", 100800102),
    MERCHANT_PASSWORD_ERROR("密码错误", 100800103),

    // 通用100900***
    ILLEGAL_OPERATION("非法操作", 100900100),



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
