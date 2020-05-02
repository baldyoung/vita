package com.baldyoung.vita.common.service;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;

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
    void clearShoppingCart(Integer shoppingCartId) throws ServiceException;

    /**
     * 删除购物车中的指定商品
     * @param shoppingCartId
     * @param productIds
     */
    void deleteProductInShoppingCart(Integer shoppingCartId, Integer... productIds) throws ServiceException;

    /**
     * 修改购物车商品
     * @param shoppingCartId
     * @param itemData
     */
    void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) throws ServiceException;

    /**
     * 获取购物车中所有的商品
     * @param shoppingCartId
     * @return
     */
    Map<Integer, Integer> getAllProductFromShoppingCart(Integer shoppingCartId) throws ServiceException;

    /**
     * 获取购物车中指定商品的数量
     * @param shoppingCartId
     * @param productId
     * @return
     */
    Integer getProductQuantityFromShoppingCart(Integer shoppingCartId, Integer productId) throws ServiceException;

    /**
     * 设置购物车中指定商品的数量
     * @param shoppingCartId
     * @param productId
     * @param quantity
     */
    void setProductQuantityForShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) throws ServiceException;

    /**
     * 整理购物车中的商品
     * @param shoppingCartId
     * @return
     */
    List<ShoppingCartItem> packageData(Integer shoppingCartId) throws ServiceException;








}
