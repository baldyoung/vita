package com.baldyoung.vita.common.pojo.entity;

public class DiningRoomEntity {

    private Integer diningRoomId;
    private String diningRoomName;
    private Integer diningRoomGrade;
    /**
     * -1 未开放
     * 0  未使用
     * 1  使用中
     * 2  清理中
     * 3  出单中
     */
    private Integer diningRoomStatus;
    private String currentBillNumber;

    private String diningRoomInfo;

    public String getDiningRoomInfo() {
        return diningRoomInfo;
    }

    public void setDiningRoomInfo(String diningRoomInfo) {
        this.diningRoomInfo = diningRoomInfo;
    }

    @Override
    public String toString() {
        return "DiningRoomEntity{" +
                "diningRoomId=" + diningRoomId +
                ", diningRoomName='" + diningRoomName + '\'' +
                ", diningRoomGrade=" + diningRoomGrade +
                ", diningRoomStatus=" + diningRoomStatus +
                ", currentBillNumber='" + currentBillNumber + '\'' +
                '}';
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
}
