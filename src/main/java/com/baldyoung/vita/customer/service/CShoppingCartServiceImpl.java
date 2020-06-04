package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.pojo.dto.product.CProductDto;
import com.baldyoung.vita.common.pojo.dto.shoppingCart.DiningData;
import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.AdvanceOrderServiceImpl;
import com.baldyoung.vita.common.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.SHOPPING_SUBMIT_EMPTY;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;

/**
 * 顾客端的购物车服务
 */
@Service
public class CShoppingCartServiceImpl {

    @Autowired
    //@Qualifier("shoppingCartServiceImpl")
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private AdvanceOrderServiceImpl advanceOrderService;

    @Autowired
    private CProductServiceImpl cProductService;

    /**
     * 往指定购物车里新增商品
     * @param shoppingCartId
     * @param productId
     * @param quantity
     */
    public void addProductIntoShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) throws ServiceException {
        Integer currentQuantity = shoppingCartService.getProductQuantityFromShoppingCart(shoppingCartId, productId);
        int result = currentQuantity.intValue() + quantity.intValue();
        if (0 > result) {
            result = 0;
        }
        shoppingCartService.setProductQuantityForShoppingCart(shoppingCartId, productId, result);
    }

    /**
     * 获取指定购物车内的所有商品数据
     * @param shoppingCartId
     * @return
     */
    public List<CProductDto> getALLItemFromShoppingCart(Integer shoppingCartId) throws ServiceException {
        Map map = shoppingCartService.getAllProductFromShoppingCart(shoppingCartId);
        //System.out.println(map);
        List<CProductDto> result;
        if (null != map && !isEmptyCollection(map.keySet())) {
            List<Integer> productIds = new ArrayList(map.keySet());
            result = cProductService.getProductWithProductIds(productIds);
            for (CProductDto dto : result) {
                //System.out.println(map.get(String.valueOf(dto.getProductId())));
                dto.setCurrentQuantity(toInteger(map.get(String.valueOf(dto.getProductId()))));
            }
        } else {
            result = new ArrayList<>(0);
        }
        return result;
    }

    /**
     * 删除购物车中的指定商品
     * @param roomId
     * @param productIds
     */
    public void deleteProduct(Integer roomId, Integer... productIds) throws ServiceException {
        shoppingCartService.deleteProductInShoppingCart(roomId, productIds);
    }

    /**
     * 锁定购物车，生成预订单
     * @param roomId
     * @param diningTime
     * @param diningType
     * @return
     * @throws ServiceException
     */
    public String readySubmitShoppingCart(Integer roomId, String diningTime, Integer diningType, List<ShoppingCartItem> itemList) throws ServiceException {
        // 锁定购物车，操作期间不允许修改购物车数据
        if (isEmptyCollection(itemList)) {
            throw new ServiceException(SHOPPING_SUBMIT_EMPTY);
        }
        DiningData diningData = new DiningData();
        diningData.setDiningType(diningType);
        diningData.setDiningTime(diningTime);
        String key = shoppingCartService.prepareSubmit(roomId, diningData);
        advanceOrderService.setAdvanceOrder(roomId, itemList);
        return key;
    }

    public void doHeartBeat(Integer roomId, String key) throws ServiceException {
        shoppingCartService.doHeartBeat(roomId, key);
    }

    public DiningData getDiningData(Integer roomId) throws ServiceException {
        return shoppingCartService.getDiningData(roomId);
    }

    public void cancelSubmitShoppingCart(Integer roomId) {
        // 结束对购物车的锁定
        //shoppingCartService.(roomId);
    }




}
