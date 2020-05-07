package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.pojo.dto.ProductAttribute.ProductAttributeDto;
import com.baldyoung.vita.common.pojo.dto.ProductAttribute.ProductAttributeTypeDto;
import com.baldyoung.vita.common.pojo.dto.order.COrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.COrderItemDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.OrderItemReceiveDto;
import com.baldyoung.vita.common.pojo.dto.product.CProductDto;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.entity.ShoppingCartItem;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.AdvanceOrderServiceImpl;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import com.baldyoung.vita.common.service.impl.OrderServiceImpl;
import com.baldyoung.vita.common.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;
import static com.baldyoung.vita.common.utility.CommonMethod.*;

@Service
public class COrderServiceImpl {

    @Autowired
    private AdvanceOrderServiceImpl advanceOrderService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private CProductServiceImpl cProductService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CProductAttributeServiceImpl cProductAttributeService;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * 获取指定就餐位的预订单数据
     * @param roomId
     * @return
     * @throws ServiceException
     */
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

    /**
     * 顾客端进行购物车中的商品下单操作
     * @param roomId
     * @param orderTypeFlag
     * @param orderPresetTime
     * @param itemReceiveDtoList
     * @return
     */
    public List<OrderItemEntity> doOrder(Integer roomId, Integer orderTypeFlag, String orderPresetTime, List<OrderItemReceiveDto> itemReceiveDtoList) throws ServiceException {
        if (isEmptyCollection(itemReceiveDtoList)) {
            throw new ServiceException(ORDER_IS_EMPTY);
        }
        List<Integer> productIds = new ArrayList(itemReceiveDtoList.size());
        // 库存图
        Map<Integer, Integer> quantityMap = new HashMap();
        for (OrderItemReceiveDto dto : itemReceiveDtoList) {
            productIds.add(dto.getProductId());
            quantityMap.put(dto.getProductId(), dto.getProductQuantity());
        }
        // 获取指定商品集的详细数据
        List<CProductDto> productList = cProductService.getProductWithProductIds(productIds);
        // 获取所有的商品属性数据
        List<ProductAttributeTypeDto> attributeList = cProductAttributeService.getAllAttributeData();
        // 商品属性图
        Map<Integer, String> attributeMap = new HashMap();
        for (ProductAttributeTypeDto dto : attributeList) {
            List<ProductAttributeDto> valueList = dto.getProductAttributeList();
            for (ProductAttributeDto attribute : valueList) {
                attributeMap.put(attribute.getProductAttributeId(), attribute.getProductAttributeName());
            }
        }
        List<OrderItemEntity> itemList = new ArrayList(productList.size());
        for (CProductDto dto : productList) {
            OrderItemEntity entity = new OrderItemEntity();
            entity.setOrderProductId(dto.getProductId());
            entity.setOrderProductName(dto.getProductName());
            entity.setOrderProductImg(dto.getProductImgName());
            entity.setOrderProductPrice(dto.getProductPrice());
            entity.setOrderProductQuantity(quantityMap.get(dto.getProductId()));
            String attributeName = attributeMap.get(dto.getProductAttributeTypeId());
            if (null == attributeName) {
                attributeName = "";
            }
            entity.setOrderProductRemarks(attributeName);
            itemList.add(entity);
        }
        List<OrderItemEntity> result = orderService.doOrder(roomId, orderTypeFlag, orderPresetTime, 1, itemList);
        shoppingCartService.deleteProductInShoppingCartWithoutSynchronized(roomId, productIds.toArray(new Integer[productIds.size()]));
        return result;
    }

    /**
     * 获取当前就餐位的最新下单结果
     * @param roomId
     * @return
     * @throws ServiceException
     */
    public COrderDto getLastOrder(Integer roomId) throws ServiceException {
        String billNumber = billService.getRoomBillNumber(roomId);
        List<OrderEntity> orderList = orderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(orderList)) {
            throw new ServiceException(NO_ORDER);
        }
        OrderEntity order = orderList.get(0);
        List<OrderItemEntity>  orderItemList = orderItemDao.selectOrderItemList(order.getOrderId());
        BillEntity billEntity = billService.getBill(billNumber);
        COrderDto result = new COrderDto();
        result.setOrderId(order.getOrderId());
        result.setBillNumber(order.getBillNumber());
        result.setOrderCreateDateTime(order.getOrderCreateDateTime());
        result.setOrderPresetTime(order.getOrderPresetTime());
        result.setOrderTypeFlag(order.getOrderTypeFlag());
        result.setOwnerName(billEntity.getBillOwnerName());
        List<COrderItemDto> cOrderItemDtoList = new ArrayList(orderItemList.size());
        for (OrderItemEntity entity : orderItemList) {
            cOrderItemDtoList.add(toCOrderItemDto(entity));
        }
        result.setItemList(cOrderItemDtoList);
        return result;
    }







}
