package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.BillDao;
import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.pojo.dto.bill.MBillDto;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.BILL_NO_FOUND;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

@Service
public class MBillServiceImpl {

    @Autowired
    private BillDao billDao;

    @Autowired
    private MOrderServiceImpl mOrderService;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private DiningRoomDao diningRoomDao;

    /**
     * 获取指定就餐位的账单详情
     * @param roomId
     * @return
     */
    public MBillDto getBillInfo(Integer roomId) throws ServiceException {
        String billNumber = billService.getRoomBillNumberWithoutCreate(roomId);
        if (isEmpty(billNumber)) {
            throw new ServiceException(BILL_NO_FOUND);
        }
        return getBillInfo(billNumber);
    }

    /**
     * 获取指定就餐位的账单详情
     * @param billNumber
     * @return
     */
    public MBillDto getBillInfo(String billNumber) {
        BillEntity billEntity = billDao.selectBill(billNumber);
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
        dto.setOrderList(mOrderService.getAllOrderInRoom(billNumber));
        return dto;
    }

    /**
     * 修改帐单信息
     * @param entity
     */
    public void updateBillInfo (BillEntity entity) {
        billDao.updateBillEntity(entity);
    }

    /**
     * 账单结账
     * @param billNumber
     * @param totalAmount
     * @param receiveAmount
     */
    public void settleAccount (String billNumber, BigDecimal totalAmount, BigDecimal receiveAmount, String remarks) {
        BillEntity bill = billDao.selectBill(billNumber);
        if (null == bill || null == bill.getBillId()) {
            return;
        }
        BillEntity newBill = new BillEntity();
        newBill.setBillId(bill.getBillId());
        newBill.setBillTotalAmount(totalAmount);
        newBill.setBillReceivedAmount(receiveAmount);
        billDao.updateBillEntity(newBill);
        DiningRoomEntity room = new DiningRoomEntity();
        room.setDiningRoomId(bill.getBillOwnerId());
        room.setCurrentBillNumber("");
        diningRoomDao.updateDiningRoom(room);
        billService.deleteBillNumberBuffer(bill.getBillOwnerId());
    }

}
