package com.baldyoung.vita.common.service.shoppingCart;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    Integer p = 1;

    static void delete() {

    }

    default void setProductForShoppingCart() {

    }

    List<Map<Integer, Integer>> getAllProductFromShoppingCar();

    default void clearShoppingCart() {

    }






}
