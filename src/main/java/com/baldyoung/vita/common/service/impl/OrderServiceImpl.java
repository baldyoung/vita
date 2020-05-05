package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单服务
 */
@Service
public class OrderServiceImpl {

    @Autowired
    private OrderDao orderDao;


    public Integer createNewOrder(String billNumber, Integer orderTypeFlag, String orderPresetTime, Integer orderInitiatorFlag) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setBillNumber(billNumber);
        orderEntity.setOrderTypeFlag(orderTypeFlag);
        orderEntity.setOrderPresetTime(orderPresetTime);
        orderEntity.setOrderInitiatorFlag(orderInitiatorFlag);
        orderDao.insertOrderEntity(orderEntity);
        return orderEntity.getOrderId();
    }




}
