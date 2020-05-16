package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao {

    void insertOrderItemList(@Param("itemList")List<OrderItemEntity> itemList);

    List<OrderItemEntity> selectOrderItemList(@Param("orderId")Integer orderId);

    List<OrderItemEntity> selectOrderItemListWithOrderIdList(@Param("orderIdList")List<Integer> orderIdList);

    void updateOrderItem(@Param("item")OrderItemEntity item);

    void updateOrderItemList(@Param("itemList")List<OrderItemEntity> item);

    List<OrderItemEntity> selectOrderItemListWithCondition(@Param("item")OrderItemEntity item);

}
