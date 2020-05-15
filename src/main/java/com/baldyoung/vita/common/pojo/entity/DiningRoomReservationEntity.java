package com.baldyoung.vita.common.pojo.entity;

import java.util.Date;

public class DiningRoomReservationEntity {
    private Integer reservationId;
    private Integer diningRoomId;
    private String customerName;
    private String reservationInfo;
    private String diningDate;
    private String diningTime;
    private Integer reservationStatus;
    private Date createDateTime;
    private Date updateDateTime;

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getDiningRoomId() {
        return diningRoomId;
    }

    public void setDiningRoomId(Integer diningRoomId) {
        this.diningRoomId = diningRoomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReservationInfo() {
        return reservationInfo;
    }

    public void setReservationInfo(String reservationInfo) {
        this.reservationInfo = reservationInfo;
    }

    public String getDiningDate() {
        return diningDate;
    }

    public void setDiningDate(String diningDate) {
        this.diningDate = diningDate;
    }

    public String getDiningTime() {
        return diningTime;
    }

    public void setDiningTime(String diningTime) {
        this.diningTime = diningTime;
    }

    public Integer getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(Integer reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
