package com.baldyoung.vita.common.pojo.dto.diningRoom;

public class CDiningRoomDto {
    private String diningRoomName;
    private Integer allItem;
    private Integer unfinishItem;
    private Integer finishItem;


    public String getDiningRoomName() {
        return diningRoomName;
    }

    public void setDiningRoomName(String diningRoomName) {
        this.diningRoomName = diningRoomName;
    }

    public Integer getAllItem() {
        return allItem;
    }

    public void setAllItem(Integer allItem) {
        this.allItem = allItem;
    }

    public Integer getUnfinishItem() {
        return unfinishItem;
    }

    public void setUnfinishItem(Integer unfinishItem) {
        this.unfinishItem = unfinishItem;
    }

    public Integer getFinishItem() {
        return finishItem;
    }

    public void setFinishItem(Integer finishItem) {
        this.finishItem = finishItem;
    }
}
