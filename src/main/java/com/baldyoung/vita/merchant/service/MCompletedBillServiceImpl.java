package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.CompletedBillDao;
import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.pojo.dto.bill.MBillDto;
import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;
import com.baldyoung.vita.common.pojo.entity.BillCountInfoEntity;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.BILL_NO_FOUND;
import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.ILLEGAL_OPERATION;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

/**
 * 已完结的账单服务
 */
@Service
public class MCompletedBillServiceImpl {

    @Autowired
    private CompletedBillDao completedBillDao;

    @Autowired
    private MCompletedOrderServiceImpl mCompletedOrderService;

    @Autowired
    private SystemOptimizationServiceImpl systemOptimizationService;


    /**
     * 获取指定就餐位的账单详情
     * @param billNumber
     * @return
     */
    public MBillDto getBillInfo(String billNumber) {
        BillEntity billEntity = completedBillDao.selectBill(billNumber);
        MBillDto dto = new MBillDto();
        dto.setBillCustomerNumber(billEntity.getBillCustomerNumber());
        dto.setBillEndDateTime(billEntity.getBillEndDateTime());
        dto.setBillId(billEntity.getBillId());
        dto.setBillNumber(billEntity.getBillNumber());
        dto.setBillOrderQuantity(billEntity.getBillOrderQuantity());
        dto.setBillOwnerId(billEntity.getBillOwnerId());
        dto.setBillOwnerName(billEntity.getBillOwnerName());
        dto.setBillOwnerTypeFlag(billEntity.getBillOwnerTypeFlag());
        dto.setBillReceivedAmount(billEntity.getBillReceivedAmount());
        dto.setBillReceivedDateTime(billEntity.getBillReceivedDateTime());
        dto.setBillRecentHandlerId(billEntity.getBillRecentHandlerId());
        dto.setBillRecentHandlerName(billEntity.getBillRecentHandlerName());
        dto.setBillRemarks(billEntity.getBillRemarks());
        dto.setBillStartDateTime(billEntity.getBillStartDateTime());
        dto.setBillTotalAmount(billEntity.getBillTotalAmount());
        dto.setBillCustomerName(billEntity.getBillCustomerName());
        dto.setOrderList(mCompletedOrderService.getAllOrderInBill(billNumber));
        return dto;
    }

    /**
     * 修改帐单信息
     * @param entity
     */
    public void updateBillInfo (BillEntity entity) {
        completedBillDao.updateBillEntity(entity);
    }

    /**
     * 为记账的账单进行结账
     * @param billNumber
     * @param totalAmount
     * @param receiveAmount
     */
    public void settleAccount (String billNumber, BigDecimal totalAmount, BigDecimal receiveAmount, String remarks) throws ServiceException {
        BillEntity bill = completedBillDao.selectBill(billNumber);
        if (null == bill || null == bill.getBillId()) {
            return;
        }
        Date newDate = new Date();
        BillEntity newBill = new BillEntity();
        newBill.setBillId(bill.getBillId());
        if (null == bill.getBillTotalAmount() || (0 == bill.getBillTotalAmount().compareTo(new BigDecimal(0))) ) {
            newBill.setBillTotalAmount(totalAmount);
        }
        if (null != bill.getBillReceivedAmount()) {
            throw new ServiceException(ILLEGAL_OPERATION);
        }
        newBill.setBillReceivedAmount(receiveAmount);
        newBill.setBillEndDateTime(newDate);
        if (null != receiveAmount) {
            newBill.setBillReceivedDateTime(newDate);
        }
        newBill.setBillRemarks(remarks);
        completedBillDao.updateBillEntity(newBill);
    }

    /**
     * 统计指定条件下的账单数量
     * @param diningRoomName
     * @param zeroFlag
     * @param unPay
     * @param finishFlag
     * @return
     */
    public Integer getBillNumberWithCondition(String diningRoomName,
                                              Boolean zeroFlag,
                                              Boolean unPay,
                                              Boolean finishFlag) {
        return completedBillDao.countWithCondition(diningRoomName, zeroFlag, unPay, finishFlag);
    }

    /**
     * 筛选符合条件的账单
     * @param diningRoomName
     * @param zeroFlag
     * @param unPay
     * @param finishFlag
     * @param startIndex
     * @param maxSize
     * @return
     */
    public List<BillEntity> getBillListWithCondition(String diningRoomName,
                                                     Boolean zeroFlag,
                                                     Boolean unPay,
                                                     Boolean finishFlag,
                                                     Integer startIndex,
                                                     Integer maxSize) {
        List<BillEntity> result = completedBillDao.selectWithCondition(diningRoomName, zeroFlag, unPay, finishFlag, startIndex, maxSize);
        return result;
    }

    /**
     * 获取当前所有账单的统计情况
     * @return
     */
    public BillCountInfoEntity getBillCountInfo() {
        BillCountInfoEntity entity = completedBillDao.countAllBillCountInfo();
        BillCountInfoEntity temp = completedBillDao.countZeroBillNumber();
        entity.setZeroBillNumber(temp.getZeroBillNumber());
        temp = completedBillDao.countUnPayBillCountInfo();
        entity.setUnPayBillNumber(temp.getUnPayBillNumber());
        entity.setTotalUnReceive(temp.getTotalUnReceive());
        return entity;
    }

}
