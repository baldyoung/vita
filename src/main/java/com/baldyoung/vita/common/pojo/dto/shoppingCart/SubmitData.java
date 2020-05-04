package com.baldyoung.vita.common.pojo.dto.shoppingCart;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;

import java.util.List;

public class SubmitData {

    private Integer diningType;
    private String diningTime;
    private List<ShoppingCartItem> itemList;

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

    public List<ShoppingCartItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ShoppingCartItem> itemList) {
        this.itemList = itemList;
    }
}
