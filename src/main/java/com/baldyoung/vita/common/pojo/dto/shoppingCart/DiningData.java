package com.baldyoung.vita.common.pojo.dto.shoppingCart;

public class DiningData {
    private Integer diningType;

    private String diningTime;

    @Override
    public String toString() {
        return "DiningData{" +
                "diningType=" + diningType +
                ", diningTime='" + diningTime + '\'' +
                '}';
    }

    public Integer getDiningType() {
        return diningType;
    }

    public void setDiningType(Integer diningType) {
        this.diningType = diningType;
    }

    public String getDiningTime() {
        return diningTime;
    }

    public void setDiningTime(String diningTime) {
        this.diningTime = diningTime;
    }
}
