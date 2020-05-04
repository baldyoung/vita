package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.ShoppingCartDataUnit;
import com.baldyoung.vita.common.pojo.ShoppingCartOptionUnit;
import com.baldyoung.vita.common.pojo.dto.shoppingCart.DiningData;
import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.ShoppingCartService;
import com.baldyoung.vita.common.utility.UniqueCodeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.SHOPPING_CART_NOT_FOUND;
import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.SHOPPING_CART_NO_POWER;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    UniqueCodeModule uniqueCodeModule;

    /**
     * 购物车并发锁
     */
    private Map<Integer, ShoppingCartOptionUnit> cartMap;

    private Map<Integer, ShoppingCartDataUnit> cartDataMap;

    private static String DEFAULT_TABLE_NAME = "shoppingCart";

    /**
     * 获取指定编号购物车在redis中对应的hash表名
     * @param shoppingCartId
     * @return
     */
    private static String toTableName(Integer shoppingCartId) {
        return DEFAULT_TABLE_NAME + shoppingCartId;
    }

    /**
     * 购物车服务的初始化
     */
    @PostConstruct
    private void init() {
        this.uniqueCodeModule = UniqueCodeModule.getInstance("bald", "young");
        cartDataMap = new HashMap();
        cartMap = new HashMap();
        // 获取所有就餐位的编号，并为每个就餐位生成一个购物车操作单元
        Integer[] diningRoomIds = {1, 2, 3, 4, 5, 6, 333};
        for (Integer diningRoomId : diningRoomIds) {
            cartMap.put(diningRoomId, new ShoppingCartOptionUnit());
            cartDataMap.put(diningRoomId, new ShoppingCartDataUnit());
        }
    }

    /**
     * 获取指定购物车的操作单元
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    private ShoppingCartOptionUnit getOptionUnit(Integer shoppingCartId) throws ServiceException {
        ShoppingCartOptionUnit unit = cartMap.get(shoppingCartId);
        if (null == unit) {
            throw new ServiceException(SHOPPING_CART_NOT_FOUND);
        }
        return unit;
    }

    /**
     * 获取指定购物车的数据单元
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    private ShoppingCartDataUnit getDataUnit(Integer shoppingCartId) throws ServiceException {
        ShoppingCartDataUnit unit = cartDataMap.get(shoppingCartId);
        if (null == unit) {
            throw new ServiceException(SHOPPING_CART_NOT_FOUND);
        }
        return unit;
    }

    /**
     * 尝试获取指定购物车的修改权限
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    private Lock readyWriteAction(Integer shoppingCartId) throws ServiceException {
        ShoppingCartOptionUnit unit = getOptionUnit(shoppingCartId);
        return unit.readyWriteAction();
    }

    /**
     * 尝试获取指定购物车的读取权限
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    private Lock readyReadAction(Integer shoppingCartId) throws ServiceException {
        ShoppingCartOptionUnit unit = getOptionUnit(shoppingCartId);
        return unit.readyReadAction();
    }

    /**
     * 清空指定购物车
     * @param shoppingCartId
     * @throws ServiceException
     */
    @Override
    public void clearShoppingCart(Integer shoppingCartId) throws ServiceException {
        Lock lock = readyWriteAction(shoppingCartId);
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId));
        lock.unlock();
    }

    /**
     * 删除指定购物车中的商品
     * @param shoppingCartId
     * @param productIds
     * @throws ServiceException
     */
    @Override
    public void deleteProductInShoppingCart(Integer shoppingCartId, Integer... productIds) throws ServiceException {
        String[] productIdArray = new String[productIds.length];
        for (int i=0; i<productIds.length; i++) {
            productIdArray[i] = String.valueOf(productIds[i]);
        }
        Lock lock = readyWriteAction(shoppingCartId);
        stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId), productIdArray);
        lock.unlock();
    }

    /**
     * 修改指定购物车中的商品
     * @param shoppingCartId
     * @param itemData
     * @throws ServiceException
     */
    @Override
    @Deprecated
    public void setProductForShoppingCart(Integer shoppingCartId, Map<Integer, Integer> itemData) throws ServiceException {
        Lock lock = readyWriteAction(shoppingCartId);
        stringRedisTemplate.opsForHash().putAll(toTableName(shoppingCartId), itemData);
        lock.unlock();
    }

    /**
     * 获取指定购物车中的所有商品
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<Integer, Integer> getAllProductFromShoppingCart(Integer shoppingCartId) throws ServiceException {
        Lock lock = readyReadAction(shoppingCartId);
        Map map = stringRedisTemplate.opsForHash().entries(toTableName(shoppingCartId));
        lock.unlock();
        return map;
    }

    /**
     * 获取特定商品在指定购物车中的数量
     * @param shoppingCartId
     * @param productId
     * @return
     * @throws ServiceException
     */
    @Override
    public Integer getProductQuantityFromShoppingCart(Integer shoppingCartId, Integer productId) throws ServiceException {
        Lock lock = readyReadAction(shoppingCartId);
        Object object = stringRedisTemplate.opsForHash().get(toTableName(shoppingCartId), String.valueOf(productId));
        lock.unlock();
        Integer currentQuantity = toInteger(object);
        if (null == currentQuantity  || currentQuantity.intValue() < 0) {
            currentQuantity = 0;
        }
        return currentQuantity;
    }

    /**
     * 修改购物车中单个商品的数量
     * @param shoppingCartId
     * @param productId
     * @param quantity
     * @throws ServiceException
     */
    @Override
    public void setProductQuantityForShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) throws ServiceException {
        if (0 >= quantity.intValue()) {
            // 如果库存为空，则直接删除该条商品记录
            Lock lock = readyWriteAction(shoppingCartId);
            stringRedisTemplate.opsForHash().delete(toTableName(shoppingCartId), String.valueOf(productId));
            lock.unlock();
            return;
        }
        Lock lock = readyWriteAction(shoppingCartId);
        stringRedisTemplate.opsForHash().put(toTableName(shoppingCartId), String.valueOf(productId), String.valueOf(quantity));
        lock.unlock();
    }

    /**
     * 锁定购物车，准备提交订单
     * @param shoppingCartId
     * @return
     * @throws ServiceException
     */
    public String prepareSubmit(Integer shoppingCartId, DiningData diningData) throws ServiceException {
        ShoppingCartOptionUnit unit = getOptionUnit(shoppingCartId);
        String newHeartBeatKey = uniqueCodeModule.getUniqueCode();
        Lock lock = unit.readySubmit(newHeartBeatKey);
        ShoppingCartDataUnit dataUnit = getDataUnit(shoppingCartId);
        dataUnit.setDiningType(diningData.getDiningType());
        dataUnit.setDiningTime(diningData.getDiningTime());
        lock.unlock();
        return newHeartBeatKey;
    }

    /**
     * 购物车心跳交互调用
     * @param shoppingCartId
     * @param key
     * @throws ServiceException
     */
    public void doHeartBeat(Integer shoppingCartId, String key) throws ServiceException {
        if (null == key) {
            throw new ServiceException(SHOPPING_CART_NO_POWER);
        }
        ShoppingCartOptionUnit unit = getOptionUnit(shoppingCartId);
        Lock lock = unit.readyDoHeartBeat(key);
        lock.unlock();
    }


    public DiningData getDiningData(Integer shoppingCartId) throws ServiceException {
        DiningData diningData = new DiningData();
        Lock lock = readyReadAction(shoppingCartId);
        ShoppingCartDataUnit unit = getDataUnit(shoppingCartId);
        diningData.setDiningTime(unit.getDiningTime());
        diningData.setDiningType(unit.getDiningType());
        lock.unlock();
        return diningData;
    }

    public void setDiningData(Integer shoppingCartId, DiningData diningData) throws ServiceException {
        Lock lock = readyWriteAction(shoppingCartId);
        ShoppingCartDataUnit unit = getDataUnit(shoppingCartId);
        unit.setDiningType(diningData.getDiningType());
        unit.setDiningTime(diningData.getDiningTime());
        lock.unlock();
    }











    @Override
    public List<ShoppingCartItem> packageData(Integer shoppingCartId) throws ServiceException {
        return null;
    }
}
