package com.baldyoung.vita.common.pojo.entity;

import java.util.Date;

public class OrderEntity {
    private Integer orderId;
    private String billNumber;
    private Integer orderProductItemQuantity;
    private Integer orderTypeFlag;
    private String orderPresetTime;
    private Integer orderInitiatorFlag;
    private Date orderCreateDateTime;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Integer getOrderProductItemQuantity() {
        return orderProductItemQuantity;
    }

    public void setOrderProductItemQuantity(Integer orderProductItemQuantity) {
        this.orderProductItemQuantity = orderProductItemQuantity;
    }

    public Integer getOrderTypeFlag() {
        return orderTypeFlag;
    }

    public void setOrderTypeFlag(Integer orderTypeFlag) {
        this.orderTypeFlag = orderTypeFlag;
    }

    public String getOrderPresetTime() {
        return orderPresetTime;
    }

    public void setOrderPresetTime(String orderPresetTime) {
        this.orderPresetTime = orderPresetTime;
    }

    public Integer getOrderInitiatorFlag() {
        return orderInitiatorFlag;
    }

    public void setOrderInitiatorFlag(Integer orderInitiatorFlag) {
        this.orderInitiatorFlag = orderInitiatorFlag;
    }

    public Date getOrderCreateDateTime() {
        return orderCreateDateTime;
    }

    public void setOrderCreateDateTime(Date orderCreateDateTime) {
        this.orderCreateDateTime = orderCreateDateTime;
    }
}
