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

    List<OrderItemEntity> selectOrderItemListWithCondition(@Param("item")OrderItemEntity item);

    void updateOrderItemListStatus(@Param("itemIdList")List<Integer> itemIdList, @Param("newStatus")Integer newStatus);

    /**
     * 修改指定订单下商品项的状态
     * @param orderId
     * @param refuseStatus 如果是该状态下的商品项则不进行状态变更
     * @param newStatus 需要变成的新状态
     */
    void setOrderItemStatusWithCondition(@Param("orderId")Integer orderId, @Param("refuseStatus")List<Integer> refuseStatus, @Param("newStatus")Integer newStatus);

    void deleteByOrderIds(@Param("orderIds")List<Integer> orderIds);
}
