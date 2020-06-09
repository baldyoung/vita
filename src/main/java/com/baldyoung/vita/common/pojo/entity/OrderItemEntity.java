package com.baldyoung.vita.common.pojo.entity;

import java.math.BigDecimal;

public class OrderItemEntity {

    private Integer orderProductItemId;
    private Integer orderId;
    private Integer ownerId;
    private Integer orderProductId;
    private String orderProductName;
    private String orderProductImg;
    private BigDecimal orderProductPrice;
    private Integer orderProductQuantity;
    private String orderProductRemarks;
    /**
     * 0 下单成功，等待商家确定
     * 1 库存不足，下单失败
     * 2 商家已确认，等待商家发货
     * 3 商家已发货，交易完成
     * 4 已删除
     */
    private Integer orderProductItemStatusFlag;
    private String orderProductItemStatusDesc;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderProductPrice() {
        return orderProductPrice;
    }

    public void setOrderProductPrice(BigDecimal orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public Integer getOrderProductItemId() {
        return orderProductItemId;
    }

    public void setOrderProductItemId(Integer orderProductItemId) {
        this.orderProductItemId = orderProductItemId;
    }

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName;
    }

    public String getOrderProductImg() {
        return orderProductImg;
    }

    public void setOrderProductImg(String orderProductImg) {
        this.orderProductImg = orderProductImg;
    }

    public Integer getOrderProductQuantity() {
        return orderProductQuantity;
    }

    public void setOrderProductQuantity(Integer orderProductQuantity) {
        this.orderProductQuantity = orderProductQuantity;
    }

    public String getOrderProductRemarks() {
        return orderProductRemarks;
    }

    public void setOrderProductRemarks(String orderProductRemarks) {
        this.orderProductRemarks = orderProductRemarks;
    }

    public Integer getOrderProductItemStatusFlag() {
        return orderProductItemStatusFlag;
    }

    public void setOrderProductItemStatusFlag(Integer orderProductItemStatusFlag) {
        this.orderProductItemStatusFlag = orderProductItemStatusFlag;
    }

    public String getOrderProductItemStatusDesc() {
        return orderProductItemStatusDesc;
    }

    public void setOrderProductItemStatusDesc(String orderProductItemStatusDesc) {
        this.orderProductItemStatusDesc = orderProductItemStatusDesc;
    }

}
