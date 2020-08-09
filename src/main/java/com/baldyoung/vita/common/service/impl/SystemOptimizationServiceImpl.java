package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.BillDao;
import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

/**
 * 系统优化服务
 * 提供系统优化服务
 */
@Service
public class SystemOptimizationServiceImpl {

    @Autowired
    private BillDao billDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public void OptimizeBillData() {
        // 从营业表中获取已完结的账单
        List<BillEntity> billEntities = billDao.selectCompletedBill();
        System.out.println(billEntities);
        if (isEmptyCollection(billEntities)) {
            return;
        }
        List<String> billNumbers = new ArrayList(billEntities.size());
        for (BillEntity entity : billEntities) {
            billNumbers.add(entity.getBillNumber());
        }
        // 从营业表中获取已完结账单的订单数据
        List<OrderEntity> orderEntities = orderDao.selectOrderByBillNumberList(billNumbers);
        if (isEmptyCollection(billEntities)) {
            // return;
        }
        List<Integer> orderIds = new ArrayList(orderEntities.size());
        for (OrderEntity entity : orderEntities) {
            orderIds.add(entity.getOrderId());
        }
        // 从营业表中获取订单详情
        List<OrderItemEntity> orderItemEntities = orderItemDao.selectOrderItemListWithOrderIdList(orderIds);

    }


}
