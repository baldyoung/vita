package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<Integer, ReadWriteLock> lockMap;

    private static String DEFAULT_TABLE_NAME = "shoppingCart";

    @PostConstruct
    private void init() {
        lockMap = new HashMap();
        // 获取所有就餐位的编号，并为每个就餐位生成读写锁
        Integer[] diningRoomIds = {1, 2, 3, 4, 5, 6};
        for (Integer diningRoomId : diningRoomIds) {
            lockMap.put(diningRoomId, new ReentrantReadWriteLock());
        }
    }

    /**
     * 获取指定就餐位的read锁
     * @param shoppingCartId
     * @return
     */
    private Lock getReadLock(Integer shoppingCartId) {
        ReadWriteLock readWriteLock = lockMap.get(shoppingCartId);
        return readWriteLock.readLock();
    }

    /**
     * 获取指定就餐位的write锁
     * @param shoppingCartId
     * @return
     */
    private Lock getWriteLock(Integer shoppingCartId) {
        ReadWriteLock readWriteLock = lockMap.get(shoppingCartId);
        return readWriteLock.writeLock();
    }

    /**
     * 线程安全
     * 清空购物车
     * @param shoppingCartId
     */
    @Override
    public void clearShoppingCart(Integer shoppingCartId) {
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId));
        lock.unlock();
    }

    /**
     * 线程安全
     * 批量设置购物车中商品的数量
     * @param shoppingCartId
     * @param itemData
     */
    @Override
    public void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) {
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        stringRedisTemplate.opsForHash().putAll(toTableName(shoppingCartId), itemData);
        lock.unlock();
    }

    /**
     * 线程安全
     * 获取指定购物车的所有商品记录
     * @param shoppingCartId
     * @return
     */
    @Override
    public Map getAllProductFromShoppingCart(Integer shoppingCartId) {
        Lock lock = getReadLock(shoppingCartId);
        lock.lock();
        Map map = stringRedisTemplate.opsForHash().entries(toTableName(shoppingCartId));
        lock.unlock();
        return map;
    }

    /**
     * 线程安全
     * 获取指定商品在购物车中的数量
     * @param shoppingCartId
     * @param productId
     * @return
     */
    @Override
    public Integer getProductQuantityFromShoppingCart(Integer shoppingCartId, Integer productId) {
        Lock lock = getReadLock(shoppingCartId);
        lock.lock();
        Object object = stringRedisTemplate.opsForHash().get(toTableName(shoppingCartId), String.valueOf(productId));
        lock.unlock();
        Integer currentQuantity = toInteger(object);
        if (null == currentQuantity  || currentQuantity.intValue() < 0) {
            currentQuantity = 0;
        }
        return currentQuantity;
    }

    /**
     * 线程安全
     * 设置单个商品在购物车中的数量
     * @param shoppingCartId
     * @param productId
     * @param quantity
     */
    @Override
    public void setProductQuantityForShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) {
        if (0 >= quantity.intValue()) {
            // 如果库存为空，则直接删除该条商品记录
            Lock lock = getWriteLock(shoppingCartId);
            lock.lock();
            stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId), String.valueOf(productId));
            lock.unlock();
            return;
        }
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        stringRedisTemplate.opsForHash().put(toTableName(shoppingCartId), String.valueOf(productId), String.valueOf(quantity));
        lock.unlock();
    }

    /**
     * 线程安全
     * 对指定购物车中的商品数据进行打包
     * @param shoppingCartId
     * @return
     */
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
