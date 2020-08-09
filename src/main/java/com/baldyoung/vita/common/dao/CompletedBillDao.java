package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.BillCountInfoEntity;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CompletedBill 表：用于存储所有已完结的账单。
 */
@Repository
public interface CompletedBillDao {

    void insertBillList(@Param("billList")List<BillEntity> billEntityList);


    void insertBillEntity(@Param("bill")BillEntity billEntity);

    BillEntity selectBill(@Param("billNumber")String billNumber);

    void updateBillEntity(@Param("bill")BillEntity bill);

    List<BillEntity> selectWithCondition(@Param("diningRoomName")String diningRoomName,
                                         @Param("zeroFlag")Boolean zeroFlag,
                                         @Param("unPay")Boolean unPay,
                                         @Param("finishFlag")Boolean finishFlag,
                                         @Param("startIndex")Integer startIndex,
                                         @Param("maxSize")Integer maxSize);

    Integer countWithCondition(@Param("diningRoomName")String diningRoomName,
                               @Param("zeroFlag")Boolean zeroFlag,
                               @Param("unPay")Boolean unPay,
                               @Param("finishFlag")Boolean finishFlag);


    /**
     * 获取所有账单的数量和总的营业额
     * @return
     */
    BillCountInfoEntity countAllBillCountInfo();

    /**
     * 获取零金额账单的数量
     * @return
     */
    BillCountInfoEntity countZeroBillNumber();

    /**
     * 获取未结账单的数量和总额度
     * @return
     */
    BillCountInfoEntity countUnPayBillCountInfo();


}
