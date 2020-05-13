package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单服务
 */
@Service
public class OrderServiceImpl {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BillServiceImpl billService;


    private Integer createNewOrder(String billNumber, Integer orderTypeFlag, String orderPresetTime, Integer orderInitiatorFlag) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setBillNumber(billNumber);
        orderEntity.setOrderTypeFlag(orderTypeFlag);
        orderEntity.setOrderPresetTime(orderPresetTime);
        orderEntity.setOrderInitiatorFlag(orderInitiatorFlag);
        orderDao.insertOrderEntity(orderEntity);
        return orderEntity.getOrderId();
    }

    private void addItemIntoOrder(Integer orderId, List<OrderItemEntity> itemList) {
        for (OrderItemEntity entity : itemList) {
            entity.setOrderId(orderId);
        }
        orderItemDao.insertOrderItemList(itemList);
    }

    /**
     * 购物车下单，提交商品项
     * @param roomId
     * @param orderTypeFlag
     * @param orderPresetTime
     * @param orderInitiatorFlag
     * @param itemList
     * @return
     */
    public List<OrderItemEntity> doOrder(Integer roomId, Integer orderTypeFlag, String orderPresetTime, Integer orderInitiatorFlag, List<OrderItemEntity> itemList) {
        String billNumber = billService.getRoomBillNumber(roomId);
        Integer orderId = createNewOrder(billNumber, orderTypeFlag, orderPresetTime, orderInitiatorFlag);
        for (OrderItemEntity entity : itemList) {
            if (null != entity.getOrderId()) {
                // 进行下单校验，校验成功的标记下单成功
                // ------------------------------------------------------------------------
            }
            entity.setOrderId(orderId);
            entity.setOrderProductItemStatusFlag(0);
            if (null == entity.getOrderProductImg()) {
                entity.setOrderProductImg("default.gif");
            }
        }
        orderItemDao.insertOrderItemList(itemList);
        return itemList;
    }






}
