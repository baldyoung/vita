package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.BillEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

/**
 * 运营中的账单服务
 */
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
        if (isEmpty(customerName) && null == customerNumber ) {
            return success();
        }
        if (null != customerName && customerName.length() >= 50) {
            return defeat("账单的客户名称字数要小于50");
        }
        BillEntity entity = new BillEntity();
        entity.setBillId(billId);
        entity.setBillCustomerName(customerName);
        entity.setBillCustomerNumber(customerNumber);
        mBillService.updateBillInfo(entity);
        return success();
    }

    /**
     * 账单结账
     * @param billNumber
     * @param totalAmount
     * @param receiveAmount
     * @param remarks
     * @return
     */
    @PostMapping("settleAccount")
    public ResponseResult settleAccount(@RequestParam("billNumber")String billNumber,
                                        @RequestParam("totalAmount")BigDecimal totalAmount,
                                        @RequestParam(value = "receiveAmount", required = false)BigDecimal receiveAmount,
                                        @RequestParam(value = "remarks", required = false)String remarks) throws ServiceException {
        if (isEmpty(billNumber) || billNumber.length() >= 30) {
            return defeat("非法数据");
        }
        if (null == remarks) {
            remarks = "";
        }
        if (remarks.length() >= 100) {
            return defeat("账单备注字数要小于100");
        }
        mBillService.settleAccount(billNumber, totalAmount, receiveAmount, remarks);
        return success();
    }

}
