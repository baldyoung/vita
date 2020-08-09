package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CompletedOrder 表：用于存储已经完结的订单
 */
@Repository
public interface CompletedOrderDao {

    /**
     * 如果想获取新增行的Id，则不能使用@Param注解
     * @param order
     */
    void insertOrderEntity(OrderEntity order);

    List<OrderEntity> selectOrderByBillNumber(@Param("billNumber")String billNumber);

    OrderEntity selectOrderByOrderId(@Param("orderId")Integer orderId);
}
