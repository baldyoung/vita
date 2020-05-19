package com.baldyoung.vita.common.pojo.entity;

import java.util.Date;

public class DiningRoomPositionEntity {

    private Integer positionId;
    private Integer diningRoomId;
    private String positionKey;
    private String requestQRCodeImgName;
    private Date createDateTime;
    private Date updateDateTime;

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getDiningRoomId() {
        return diningRoomId;
    }

    public void setDiningRoomId(Integer diningRoomId) {
        this.diningRoomId = diningRoomId;
    }

    public String getPositionKey() {
        return positionKey;
    }

    public void setPositionKey(String positionKey) {
        this.positionKey = positionKey;
    }

    public String getRequestQRCodeImgName() {
        return requestQRCodeImgName;
    }

    public void setRequestQRCodeImgName(String requestQRCodeImgName) {
        this.requestQRCodeImgName = requestQRCodeImgName;
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
