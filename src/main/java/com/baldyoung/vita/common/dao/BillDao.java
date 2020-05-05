package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.BillEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDao {

    void insertBillEntity(@Param("bill")BillEntity billEntity);


}
