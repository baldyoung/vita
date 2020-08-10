package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.*;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private CompletedBillDao completedBillDao;
    @Autowired
    private CompletedOrderItemDao completedOrderItemDao;
    @Autowired
    private CompletedOrderDao completedOrderDao;

    private ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    private void init() {
        threadPoolExecutor = new ThreadPoolExecutor(
                2,
                20,
                180,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(64),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    public void submitWorkAsynchronously(Runnable runnable) {
        this.threadPoolExecutor.submit(runnable);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public void OptimizeBillData() {
        System.out.println("开始账单数据迁移");
        // 从营业表中获取已完结的账单
        List<BillEntity> billEntities = billDao.selectCompletedBill();
        if (isEmptyCollection(billEntities)) {
            return;
        }
        List<String> billNumbers = new ArrayList(billEntities.size());
        List<Integer> billIds = new ArrayList(billEntities.size());
        for (BillEntity entity : billEntities) {
            billNumbers.add(entity.getBillNumber());
            billIds.add(entity.getBillId());
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
        // 从营业表中获取已完结账单的订单详情
        List<OrderItemEntity> orderItemEntities;
        if (!isEmptyCollection(orderIds)) {
            orderItemEntities = orderItemDao.selectOrderItemListWithOrderIdList(orderIds);
        } else {
            orderItemEntities = new ArrayList(0);
        }

        // ---- 整理数据，并将其存储到完结表中
        completedBillDao.insertBillList(billEntities);
        if (!isEmptyCollection(orderEntities)) {
            completedOrderDao.insertOrderEntityList(orderEntities);
        }
        if (!isEmptyCollection(orderItemEntities)) {
            completedOrderItemDao.insertOrderItemList(orderItemEntities);
        }
        // ---- 删除原营业表中的数据
        billDao.deleteBillList(billIds);
        orderDao.deleteByOrderIds(orderIds);
        orderItemDao.deleteByOrderIds(orderIds);
    }


}
