package com.baldyoung.vita.common.pojo;

/**
 * 购物车数据单元
 */
public class ShoppingCartDataUnit {
    /**
     * 就餐时间
     */
    private String diningTime;
    /**
     * 就餐方式
     */
    private Integer diningType;
    /**
     * 就餐位置
     */
    private Object diningAddress;

    public String getDiningTime() {
        return diningTime;
    }

    public void setDiningTime(String diningTime) {
        this.diningTime = diningTime;
    }

    public Integer getDiningType() {
        return diningType;
    }

    public void setDiningType(Integer diningType) {
        this.diningType = diningType;
    }

    public Object getDiningAddress() {
        return diningAddress;
    }

    public void setDiningAddress(Object diningAddress) {
        this.diningAddress = diningAddress;
    }
}
