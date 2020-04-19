package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CShoppingCartServiceImpl {

    @Autowired
    //@Qualifier("shoppingCartServiceImpl")
    private ShoppingCartService shoppingCartService;

    public void addProductIntoShoppingCart(Integer shoppingCartId, Integer productId, Integer quantity) {
        Integer currentQuantity = shoppingCartService.getProductQuantityFromShoppingCart(shoppingCartId, productId);
        int result = currentQuantity.intValue() + quantity.intValue();
        if (0 >= result) {
            result = 0;
        }
        shoppingCartService.setProductQuantityForShoppingCart(shoppingCartId, productId, result);
    }

    public Map getALLItemFromShoppingCart(Integer shoppingCartId) {
        Map map = shoppingCartService.getAllProductFromShoppingCart(shoppingCartId);
        if (null == map) {
            map = new HashMap();
        }
        return map;
    }

}
