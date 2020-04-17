package com.baldyoung.vita.common.service;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    Integer p = 1;

    static void init() {

    }

    default void test(){

    }

    /**
     * 清空购物车
     * @param shoppingCartId
     */
    void clearShoppingCart(Integer shoppingCartId);

    /**
     *
     * @param shoppingCartId
     * @param itemList
     */
    void setProductForShoppingCart(Integer shoppingCartId, List<ShoppingCartItem> itemList);

    Map<Integer, Integer> getAllProductFromShoppingCar(Integer shoppingCartId);

    List<ShoppingCartItem> packageData(Integer shoppingCartId);








}
