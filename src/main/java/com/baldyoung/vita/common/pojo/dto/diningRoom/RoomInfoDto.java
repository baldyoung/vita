package com.baldyoung.vita.common.pojo.dto.diningRoom;

import java.math.BigDecimal;

public class RoomInfoDto {
    private Integer diningRoomId;
    private String diningRoomName;
    private Integer diningRoomGrade;
    private Integer diningRoomStatus;
    private String currentBillNumber;
    private String diningRoomInfo;
    private String reservationData;

    private Integer customerNumber;
    private BigDecimal billAmount;

    public String getReservationData() {
        return reservationData;
    }

    public void setReservationData(String reservationData) {
        this.reservationData = reservationData;
    }

    public Integer getDiningRoomId() {
        return diningRoomId;
    }

    public void setDiningRoomId(Integer diningRoomId) {
        this.diningRoomId = diningRoomId;
    }

    public String getDiningRoomName() {
        return diningRoomName;
    }

    public void setDiningRoomName(String diningRoomName) {
        this.diningRoomName = diningRoomName;
    }

    public Integer getDiningRoomGrade() {
        return diningRoomGrade;
    }

    public void setDiningRoomGrade(Integer diningRoomGrade) {
        this.diningRoomGrade = diningRoomGrade;
    }

    public Integer getDiningRoomStatus() {
        return diningRoomStatus;
    }

    public void setDiningRoomStatus(Integer diningRoomStatus) {
        this.diningRoomStatus = diningRoomStatus;
    }

    public String getCurrentBillNumber() {
        return currentBillNumber;
    }

    public void setCurrentBillNumber(String currentBillNumber) {
        this.currentBillNumber = currentBillNumber;
    }

    public String getDiningRoomInfo() {
        return diningRoomInfo;
    }

    public void setDiningRoomInfo(String diningRoomInfo) {
        this.diningRoomInfo = diningRoomInfo;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }
}
