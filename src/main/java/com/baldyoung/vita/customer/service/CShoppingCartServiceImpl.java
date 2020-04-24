package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.product.CProductDto;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;

/**
 * 顾客端的购物车服务
 */
@Service
public class CShoppingCartServiceImpl {

    @Autowired
    //@Qualifier("shoppingCartServiceImpl")
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CProductServiceImpl cProductService;

    /**
     * 往指定购物车里新增商品
     * @param shoppingCartId
     * @param productId
     * @param quantity
     */
    public void addProductIntoShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) {
        Integer currentQuantity = shoppingCartService.getProductQuantityFromShoppingCart(shoppingCartId, productId);
        int result = currentQuantity.intValue() + quantity.intValue();
        if (0 >= result) {
            result = 0;
        }
        shoppingCartService.setProductQuantityForShoppingCart(shoppingCartId, productId, result);
    }

    /**
     * 获取指定购物车内的所有商品数据
     * @param shoppingCartId
     * @return
     */
    public List<CProductDto> getALLItemFromShoppingCart(Integer shoppingCartId) {
        Map map = shoppingCartService.getAllProductFromShoppingCart(shoppingCartId);
        System.out.println(map);
        List<CProductDto> result;
        if (null != map && !isEmptyCollection(map.keySet())) {
            List<Integer> productIds = new ArrayList(map.keySet());
            result = cProductService.getProductWithProductIds(productIds);
            for (CProductDto dto : result) {
                System.out.println(map.get(String.valueOf(dto.getProductId())));
                dto.setCurrentQuantity(toInteger(map.get(String.valueOf(dto.getProductId()))));
            }
        } else {
            result = new ArrayList<>(0);
        }
        return result;
    }

}
