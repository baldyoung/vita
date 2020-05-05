package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {

    void insertOrderEntity(@Param("order")OrderEntity orderEntity);


}
