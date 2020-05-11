package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.BillDao;
import com.baldyoung.vita.common.pojo.dto.bill.MBillDto;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.BILL_NO_FOUND;

@Service
public class MBillServiceImpl {

    @Autowired
    private BillDao billDao;

    @Autowired
    private MOrderServiceImpl mOrderService;

    @Autowired
    private BillServiceImpl billService;

    /**
     * 获取指定就餐位的账单详情
     * @param roomId
     * @return
     */
    public MBillDto getBillInfo(Integer roomId) throws ServiceException {
        String billNumber = billService.getRoomBillNumberWithoutCreate(roomId);
        if (null == billNumber) {
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

}
