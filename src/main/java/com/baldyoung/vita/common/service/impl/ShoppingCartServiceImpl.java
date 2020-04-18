package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private ReadWriteLock readWriteLock;

    @PostConstruct
    private void init() {
        readWriteLock = new ReentrantReadWriteLock();
    }

    private static String DEFAULT_TABLE_NAME = "shoppingCart";

    @Override
    public void clearShoppingCart(Integer shoppingCartId) {
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId));
    }

    @Override
    public void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) {
        stringRedisTemplate.opsForHash().putAll(toTableName(shoppingCartId), itemData);
    }

    @Override
    public Map<Integer, Integer> getAllProductFromShoppingCar(Integer shoppingCartId) {
        Map<Integer, Integer> map = stringRedisTemplate.<Integer, Integer>opsForHash().entries(toTableName(shoppingCartId));
        return map;
    }

    @Override
    public List<ShoppingCartItem> packageData(Integer shoppingCartId) {
        Map<Integer, Integer> map = getAllProductFromShoppingCar(shoppingCartId);
        List<ShoppingCartItem> list = new ArrayList(map.keySet().size());
        for(Map.Entry<Integer, Integer> item : map.entrySet()) {
            if (null == item.getValue() || 0 >= item.getValue().intValue()) {
                continue;
            }
            ShoppingCartItem cell = new ShoppingCartItem();
            cell.setProductId(item.getKey());
            cell.setProductQuantity(item.getValue());
            list.add(cell);
        }
        return list;
    }

    /**
     * 获取指定编号购物车在redis中对应的hash表名
     * @param shoppingCartId
     * @return
     */
    private static String toTableName(Integer shoppingCartId) {
        return DEFAULT_TABLE_NAME + shoppingCartId;
    }
}
