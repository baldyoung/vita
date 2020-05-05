package com.baldyoung.vita.common.pojo.entity;

public class OrderItemEntity {

    private Integer orderProductItemId;
    private Integer orderProductId;
    private String orderProductName;
    private String orderProductImg;
    private Integer orderProductQuantity;
    private String orderProductRemarks;
    private Integer orderProductItemStatusFlag;
    private String orderProductItemStatusDesc;

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
