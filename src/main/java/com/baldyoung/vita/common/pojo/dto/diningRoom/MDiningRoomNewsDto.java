package com.baldyoung.vita.common.pojo.dto.diningRoom;

public class MDiningRoomNewsDto {

    private Integer roomId;
    private Integer orderNewsNumber;
    private Integer customerMessageNewsNumber;
    private Integer reservationNewsNumber;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getOrderNewsNumber() {
        return orderNewsNumber;
    }

    public void setOrderNewsNumber(Integer orderNewsNumber) {
        this.orderNewsNumber = orderNewsNumber;
    }

    public Integer getCustomerMessageNewsNumber() {
        return customerMessageNewsNumber;
    }

    public void setCustomerMessageNewsNumber(Integer customerMessageNewsNumber) {
        this.customerMessageNewsNumber = customerMessageNewsNumber;
    }

    public Integer getReservationNewsNumber() {
        return reservationNewsNumber;
    }

    public void setReservationNewsNumber(Integer reservationNewsNumber) {
        this.reservationNewsNumber = reservationNewsNumber;
    }
}
