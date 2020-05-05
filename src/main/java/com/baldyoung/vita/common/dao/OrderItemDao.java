package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao {

    void insertOrderItemList(@Param("itemList")List<OrderItemEntity> itemList);
}
