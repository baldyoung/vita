package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

@RestController
@RequestMapping("mBill")
public class MBillController {

    @Autowired
    private MBillServiceImpl mBillService;

    @GetMapping("billInfo")
    public ResponseResult getBillInfo(@RequestParam("roomId")Integer roomId) throws ServiceException {
        return success(mBillService.getBillInfo(roomId));
    }

    @PostMapping("updateSimpleInfo")
    public ResponseResult updateSimpleInfo(@RequestParam(value = "customerName", required = false)String customerName,
                                           @RequestParam(value = "customerNumber", required = false)Integer customerNumber,
                                           @RequestParam("billId")Integer billId) {
        if (isEmpty(customerName) && null == customerNumber) {
            return success();
        }
        BillEntity entity = new BillEntity();
        entity.setBillId(billId);
        entity.setBillCustomerName(customerName);
        entity.setBillCustomerNumber(customerNumber);
        mBillService.updateBillInfo(entity);
        return success();
    }
}
