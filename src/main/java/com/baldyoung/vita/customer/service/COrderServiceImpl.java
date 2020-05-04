package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.pojo.dto.product.CProductDto;
import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.AdvanceOrderServiceImpl;
import com.baldyoung.vita.common.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.SHOPPING_CART_EMPTY;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;
import static com.baldyoung.vita.common.utility.CommonMethod.toInteger;

@Service
public class COrderServiceImpl {

    @Autowired
    private AdvanceOrderServiceImpl advanceOrderService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private CProductServiceImpl cProductService;


    public List<CProductDto> getAdvanceOrder(Integer roomId) throws ServiceException {
        Map map = shoppingCartService.getAllProductFromShoppingCart(roomId);
        if (null == map || isEmptyCollection(map.keySet())) {
            throw new ServiceException(SHOPPING_CART_EMPTY);
        }
        List<ShoppingCartItem> itemList = advanceOrderService.getAdvanceOrder(roomId);
        if (null == itemList || isEmptyCollection(itemList)) {
            throw new ServiceException(SHOPPING_CART_EMPTY);
        }
        List<Integer> productIds = new ArrayList(itemList.size());
        for (ShoppingCartItem item : itemList) {
            productIds.add(item.getProductId());
        }
        List<CProductDto> result = cProductService.getProductWithProductIds(productIds);
        for (CProductDto dto : result) {
            dto.setCurrentQuantity(toInteger(map.get(String.valueOf(dto.getProductId()))));
        }
        return result;
    }
}
