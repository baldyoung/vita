package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.ILLEGAL_OPERATION;
import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.PRODUCT_ID_IS_NULL;

/**
 * 商品库存服务
 * 【库存存储位置：redis】
 */
@Service
public class ProductStockServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private CommonConfig commonConfig;

    private ReadWriteLock readWriteLock;

    /**
     * redis中，存储商品库存数据的HASH表名
     */
    private static String REDIS_MAP_NAME = "productStockMap";

    /**
     * 将商品编号转换为redis的key
     * @param productId
     * @return
     * @throws ServiceException
     */
    private String toProductKey(Integer productId) throws ServiceException {
        if (null == productId) {
            throw new ServiceException(PRODUCT_ID_IS_NULL);
        }
        return productId.toString();
    }

    /**
     * 库存服务的初始化调用
     */
    @PostConstruct
    public void init() {
        readWriteLock = new ReentrantReadWriteLock();

    }

    /**
     * 批量设置库存
     * 【批量设置库存时不会删除原库存数据】
     * @param productStockMap
     */
    public void setStockMap(Map<Integer, Integer> productStockMap) throws ServiceException {
        HashMap<String, String> map = new HashMap();
        for (Map.Entry<Integer, Integer> entry : productStockMap.entrySet()) {
            map.put(toProductKey(entry.getKey()), String.valueOf(entry.getValue()));
        }
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().putAll(REDIS_MAP_NAME, map);
        lock.unlock();
    }

    /**
     * 获取当前所有的商品库存数据
     * @return
     */
    public Map<Integer, Integer> getStockMap() {
        Map<Integer, Integer> currentStockMap = new HashMap();
        Lock lock = readWriteLock.readLock();
        lock.lock();
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(REDIS_MAP_NAME);
        lock.unlock();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Integer productId = Integer.parseInt(String.valueOf(entry.getKey()));
            Integer currentStock = Integer.parseInt(String.valueOf(entry.getValue()));
            currentStockMap.put(productId, currentStock);
        }
        return currentStockMap;
    }

    /**
     * 取消所有商品的库存限制
     * 【从redis中删除所有商品的库存字段】
     */
    public void cancelAllStock() {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().delete(REDIS_MAP_NAME);
        lock.unlock();
    }

    /**
     * 取消商品的库存限制
     * 【删除redis中指定商品的库存字段】
     * @param productId
     */
    public void cancelStock(Integer productId) throws ServiceException {
        String key = toProductKey(productId);
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().delete(REDIS_MAP_NAME, key);
        lock.unlock();
    }

    /**
     * 修改指定商品的库存
     * @param productId
     * @param newStock
     */
    public void changeStock(Integer productId, Integer newStock) throws ServiceException {
        String key = toProductKey(productId);
        String stock = String.valueOf(newStock);
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        stringRedisTemplate.opsForHash().put(REDIS_MAP_NAME, key, stock);
        lock.unlock();
    }

    /***
     * 获取指定商品的库存
     * 返回null代表该商品没有库存限制
     * @param productId
     * @return
     * @throws ServiceException
     */
    public Integer getStock(Integer productId) throws ServiceException {
        String key = toProductKey(productId);
        Lock lock = readWriteLock.readLock();
        lock.lock();
        Object object = stringRedisTemplate.opsForHash().get(REDIS_MAP_NAME, key);
        lock.unlock();
        if (null != object) {
            return Integer.parseInt(String.valueOf(object));
        }
        return null;
    }

    /**
     * 删减库存
     * 返回true表示删减成功；返回false表示删减失败；
     * @param productId
     * @param needStockNumber
     * @return
     */
    public boolean pruneStock(Integer productId, int needStockNumber) throws ServiceException {
        if (0 >= needStockNumber) {
            throw new ServiceException(ILLEGAL_OPERATION);
        }
        String key = toProductKey(productId);
        Integer stock = getStock(productId);
        if (null == stock) {
            return true;
        }
        int currentStock = stock.intValue();
        if (currentStock < needStockNumber) {
            return false;
        }
        currentStock = currentStock - needStockNumber;
        changeStock(productId, currentStock);
        return true;
    }

}
