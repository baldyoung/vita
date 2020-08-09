package com.baldyoung.vita.common.dao;

import com.baldyoung.vita.common.pojo.entity.BillCountInfoEntity;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDao {

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



    /**
     * 获取已完结的账单
     * @return
     */
    List<BillEntity> selectCompletedBill();

    /**
     * 删除指定编号的账单
     * @param billIdList
     */
    void deleteBillList(@Param("billIdList")List<Integer> billIdList);





}
