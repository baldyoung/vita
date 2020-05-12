package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.PRODUCT_NOT_FOUND;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class MOrderServiceImpl {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private ProductDao productDao;

    /**
     * 获取指定就餐位的所有订单
     * @param billNumber
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(String billNumber) {
        List<OrderEntity> entityList = orderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(entityList)) {
            return new ArrayList(0);
        }
        List<Integer> orderIds = new ArrayList(entityList.size());
        for (OrderEntity entity : entityList) {
            orderIds.add(entity.getOrderId());
        }
        List<MOrderDto> result = new ArrayList(entityList.size());
        List<OrderItemEntity> itemEntityList = orderItemDao.selectOrderItemListWithOrderIdList(orderIds);
        for (OrderEntity entity : entityList) {
            MOrderDto dto = new MOrderDto();
            Integer orderId = entity.getOrderId();
            dto.setBillNumber(entity.getBillNumber());
            dto.setOrderCreateDateTime(entity.getOrderCreateDateTime());
            dto.setOrderId(entity.getOrderId());
            dto.setOrderInitiatorFlag(entity.getOrderInitiatorFlag());
            dto.setOrderPresetTime(entity.getOrderPresetTime());
            dto.setOrderTypeFlag(entity.getOrderTypeFlag());
            List<MOrderItemDto> itemList = new LinkedList();
            for (OrderItemEntity itemEntity : itemEntityList) {
                Integer itemOrderId = itemEntity.getOrderId();
                if (itemOrderId.equals(orderId)) {
                    MOrderItemDto itemDto = new MOrderItemDto();
                    itemDto.setOrderProductId(itemEntity.getOrderProductId());
                    itemDto.setOrderProductImg(itemEntity.getOrderProductImg());
                    itemDto.setOrderProductItemId(itemEntity.getOrderProductItemId());
                    itemDto.setOrderProductItemStatusDesc(itemEntity.getOrderProductItemStatusDesc());
                    itemDto.setOrderProductItemStatusFlag(itemEntity.getOrderProductItemStatusFlag());
                    itemDto.setOrderProductName(itemEntity.getOrderProductName());
                    itemDto.setOrderProductPrice(itemEntity.getOrderProductPrice());
                    itemDto.setOrderProductQuantity(itemEntity.getOrderProductQuantity());
                    itemDto.setOrderProductRemarks(itemEntity.getOrderProductRemarks());
                    itemList.add(itemDto);
                }
            }
            dto.setItemList(itemList);
            result.add(dto);
        }
        return result;
    }
    /**
     * 获取指定就餐位的所有订单
     * @param roomId
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(Integer roomId) {
        String billNumber = billService.getRoomBillNumber(roomId);
        return getAllOrderInRoom(billNumber);
    }

    /**
     * 修改指定订单项的内容
     * @param entity
     */
    public void updateOrderItem(OrderItemEntity entity) {
        orderItemDao.updateOrderItem(entity);
    }

    /**
     * 修改指定订单集合的内容
     * @param entityList
     */
    public void updateOrderItemList(List<OrderItemEntity> entityList) {
        orderItemDao.updateOrderItemList(entityList);
    }

    /**
     * 替换订单商品
     * @param itemId
     * @param productId
     * @throws ServiceException
     */
    public void changeOrderItemProduct(Integer itemId, Integer productId) throws ServiceException {
        ProductEntity productEntity = productDao.findProductByProductId(productId);
        if (null == productEntity || null == productEntity.getProductId()) {
            throw new ServiceException(PRODUCT_NOT_FOUND);
        }
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductId(productId);
        entity.setOrderProductName(productEntity.getProductName());
        entity.setOrderProductImg(productEntity.getProductImgName());
        entity.setOrderProductPrice(productEntity.getProductPrice());
        orderItemDao.updateOrderItem(entity);
    }

}
