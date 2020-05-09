package com.baldyoung.vita.common.pojo.entity;

import java.util.Date;

public class CustomerMessageEntity {

    private Integer customerMessageId;
    private Integer diningRoomId;
    private Integer customerMessageTypeId;
    private String customerMessageTypeName;
    private String customerMessageValue;
    private Integer customerMessageStatus;
    private Date createDateTime;

    public Integer getDiningRoomId() {
        return diningRoomId;
    }

    public void setDiningRoomId(Integer diningRoomId) {
        this.diningRoomId = diningRoomId;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Integer getCustomerMessageId() {
        return customerMessageId;
    }

    public void setCustomerMessageId(Integer customerMessageId) {
        this.customerMessageId = customerMessageId;
    }

    public Integer getCustomerMessageTypeId() {
        return customerMessageTypeId;
    }

    public void setCustomerMessageTypeId(Integer customerMessageTypeId) {
        this.customerMessageTypeId = customerMessageTypeId;
    }

    public String getCustomerMessageTypeName() {
        return customerMessageTypeName;
    }

    public void setCustomerMessageTypeName(String customerMessageTypeName) {
        this.customerMessageTypeName = customerMessageTypeName;
    }

    public String getCustomerMessageValue() {
        return customerMessageValue;
    }

    public void setCustomerMessageValue(String customerMessageValue) {
        this.customerMessageValue = customerMessageValue;
    }

    public Integer getCustomerMessageStatus() {
        return customerMessageStatus;
    }

    public void setCustomerMessageStatus(Integer customerMessageStatus) {
        this.customerMessageStatus = customerMessageStatus;
    }
}
