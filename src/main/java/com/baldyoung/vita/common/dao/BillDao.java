package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.BillEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDao {

    void insertBillEntity(@Param("bill")BillEntity billEntity);

    BillEntity selectBill(@Param("billNumber")String billNumber);

    void updateBillEntity(@Param("bill")BillEntity bill);

    List<BillEntity> selectWithCondition(@Param("diningRoomName")String diningRoomName, @Param("zeroFlag")Boolean zeroFlag, @Param("unPay")Boolean unPay, @Param("finishFlag")Boolean finishFlag);



}
