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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;

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

    private void getReadLock() {
        readWriteLock.readLock();
    }

    private void getWriteLock() {
        readWriteLock.writeLock();
    }

    @Override
    public void clearShoppingCart(Integer shoppingCartId) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId));
        lock.unlock();
    }

    @Override
    public void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().putAll(toTableName(shoppingCartId), itemData);
        lock.unlock();
    }

    @Override
    public Map getAllProductFromShoppingCart(Integer shoppingCartId) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        Map map = stringRedisTemplate.opsForHash().entries(toTableName(shoppingCartId));
        lock.unlock();
        return map;
    }

    @Override
    public Integer getProductQuantityFromShoppingCart(Integer shoppingCartId, Integer productId) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        Object object = stringRedisTemplate.opsForHash().get(toTableName(shoppingCartId), String.valueOf(productId));
        lock.unlock();
        Integer currentQuantity = toInteger(object);
        if (null == currentQuantity) {
            currentQuantity = 0;
        }
        return currentQuantity;
    }

    @Override
    public void setProductQuantityForShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().put(toTableName(shoppingCartId), String.valueOf(productId), String.valueOf(quantity));
        lock.unlock();
    }

    @Override
    public List<ShoppingCartItem> packageData(Integer shoppingCartId) {
        Map<Integer, Integer> map = getAllProductFromShoppingCart(shoppingCartId);
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
