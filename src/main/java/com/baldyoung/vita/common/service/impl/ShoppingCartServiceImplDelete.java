package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.SHOPPING_CART_PRE_ORDER;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;


public class ShoppingCartServiceImplDelete implements ShoppingCartService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 购物车并发锁
     */
    private Map<Integer, ReadWriteLock> lockMap;
    /**
     * 下单并发锁
     *  如果有下单操作正在进行，则对应的value为Boolean.TRUE
     */
    private Map<Integer, Boolean> orderStatusMap;

    private static String DEFAULT_TABLE_NAME = "shoppingCart";

    @PostConstruct
    private void init() {
        lockMap = new HashMap();
        orderStatusMap = new HashMap();
        // 获取所有就餐位的编号，并为每个就餐位生成读写锁
        Integer[] diningRoomIds = {1, 2, 3, 4, 5, 6, 333};
        for (Integer diningRoomId : diningRoomIds) {
            lockMap.put(diningRoomId, new ReentrantReadWriteLock());
            orderStatusMap.put(diningRoomId, Boolean.FALSE);
        }
    }

    /**
     * 获取指定编号购物车在redis中对应的hash表名
     * @param shoppingCartId
     * @return
     */
    private static String toTableName(Integer shoppingCartId) {
        return DEFAULT_TABLE_NAME + shoppingCartId;
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
     * 尝试修改购物车数据的前置准备
     * @param shoppingCartId
     * @throws ServiceException
     */
    public void readyUpdateShoppingCart(Integer shoppingCartId) throws ServiceException {
        //Lock lock = getWriteLock(shoppingCartId);
        //lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
             // lock.unlock();
             throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
        //lock.unlock();
    }

    /**
     * 尝试提交预订单时的前置准备，对购物车进行锁定，拒绝所有修改操作
     * @param shoppingCartId
     * @throws ServiceException
     */
    public void readyPreOrderShoppingCart(Integer shoppingCartId) throws ServiceException {
        Lock lock = getWriteLock(shoppingCartId);
        //lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
            // 如果已经锁定，则抛出异常，拒绝再次锁定
            // lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
        orderStatusMap.put(shoppingCartId, Boolean.TRUE);
        //lock.unlock();
    }

    /**
     * 关闭对购物车的锁定
     * @param shoppingCartId
     */
    public void cancelPreOrderShoppingCart(Integer shoppingCartId) {
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        orderStatusMap.put(shoppingCartId, Boolean.FALSE);
        lock.unlock();
    }

    /**
     * 线程安全
     * 清空购物车
     * @param shoppingCartId
     */
    @Override
    public void clearShoppingCart(Integer shoppingCartId) throws ServiceException {
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId));
        lock.unlock();
    }

    @Override
    public void deleteProductInShoppingCart(Integer shoppingCartId, Integer... productIds) throws ServiceException {
        String[] productIdArray = new String[productIds.length];
        for (int i=0; i<productIds.length; i++) {
            productIdArray[i] = String.valueOf(productIds[i]);
        }
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId), productIdArray);
        lock.unlock();
    }

    /**
     * 可能需要将itemData从Map<Integer, Integer>转换为Map<String, String>才能好使
     * 线程安全
     * 批量设置购物车中商品的数量
     * @param shoppingCartId
     * @param itemData
     */
    @Deprecated
    @Override
    public void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) throws ServiceException {
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
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
    public void setProductQuantityForShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) throws ServiceException {
        if (0 >= quantity.intValue()) {
            // 如果库存为空，则直接删除该条商品记录
            Lock lock = getWriteLock(shoppingCartId);
            lock.lock();
            Boolean status = orderStatusMap.get(shoppingCartId);
            if (Boolean.TRUE == status) {
                lock.unlock();
                throw new ServiceException(SHOPPING_CART_PRE_ORDER);
            }
            stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId), String.valueOf(productId));
            lock.unlock();
            return;
        }
        Lock lock = getWriteLock(shoppingCartId);
        lock.lock();
        Boolean status = orderStatusMap.get(shoppingCartId);
        if (Boolean.TRUE == status) {
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
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
    public List<ShoppingCartItem> packageData(Integer shoppingCartId) throws ServiceException {
        Lock lock = getReadLock(shoppingCartId);
        lock.lock();
        Map map = stringRedisTemplate.opsForHash().entries(toTableName(shoppingCartId));
        lock.unlock();
        List<ShoppingCartItem> list = new ArrayList(map.keySet().size());
        Set<Map.Entry<Object, Object>> set = map.entrySet();
        for(Map.Entry<Object, Object> item : set) {
            Integer quantity = toInteger(item.getValue());
            Integer cartId = toInteger(item.getKey());
            if (null == quantity || 0 >= quantity.intValue()) {
                continue;
            }
            ShoppingCartItem cell = new ShoppingCartItem();
            cell.setProductId(cartId);
            cell.setProductQuantity(quantity);
            list.add(cell);
        }
        return list;
    }




}