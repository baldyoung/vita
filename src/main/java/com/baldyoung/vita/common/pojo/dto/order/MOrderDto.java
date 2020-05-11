package com.baldyoung.vita.common.pojo.dto.order;

import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;

import java.util.Date;
import java.util.List;

public class MOrderDto {
    private Integer orderId;
    private String billNumber;
    private Integer orderProductItemQuantity;
    private Integer orderTypeFlag;
    private String orderPresetTime;
    private Integer orderInitiatorFlag;
    private Date orderCreateDateTime;
    private String ownerName;
    private List<MOrderItemDto> itemList;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }



    public List<MOrderItemDto> getItemList() {
        return itemList;
    }

    public void setItemList(List<MOrderItemDto> itemList) {
        this.itemList = itemList;
    }

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
