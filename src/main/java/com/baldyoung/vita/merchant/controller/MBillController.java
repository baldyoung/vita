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
                                        @RequestParam("remarks")String remarks) throws ServiceException {
        if (isEmpty(billNumber) || billNumber.length() >= 30) {
            return defeat("非法数据");
        }
        if (null != remarks && remarks.length() >= 100) {
            return defeat("账单备注字数要小于100");
        }
        mBillService.settleAccount(billNumber, totalAmount, receiveAmount, remarks);
        return success();
    }

    /**
     * 获取符合指定条件的账单的数量
     * @param diningRoomName
     * @param zeroFlag
     * @param unPay
     * @param finishFlag
     * @return
     */
    @PostMapping("billNumberWithCondition")
    public ResponseResult getBillNumberWithCondition(@RequestParam(value = "diningRoomName", required = false)String diningRoomName,
                                                     @RequestParam("zeroFlag")Integer zeroFlag,
                                                     @RequestParam("unPay")Integer unPay,
                                                     @RequestParam("finishFlag")Integer finishFlag) {
        if (isEmpty(diningRoomName)) {
            diningRoomName = null;
        }
        if (null != diningRoomName && diningRoomName.length() > 10) {
            return defeat("就餐位名称不能超过10个字");
        }
        Boolean zf = (0 != zeroFlag.intValue() ? true : null);
        Boolean up = (0 != unPay.intValue() ? true : null);
        Boolean ff = (0 != finishFlag.intValue() ? true : null);
        return success(mBillService.getBillNumberWithCondition(diningRoomName, zf, up, ff));
    }

    /**
     * 获取符合指定条件的账单数据
     * @param diningRoomName
     * @param zeroFlag
     * @param unPay
     * @param finishFlag
     * @param startIndex
     * @param maxSize
     * @return
     */
    @PostMapping("billListWithCondition")
    public ResponseResult getBillListWithCondition(@RequestParam(value = "diningRoomName", required = false)String diningRoomName,
                                                   @RequestParam("zeroFlag")Integer zeroFlag,
                                                   @RequestParam("unPay")Integer unPay,
                                                   @RequestParam("finishFlag")Integer finishFlag,
                                                   @RequestParam("startIndex")Integer startIndex,
                                                   @RequestParam("maxSize")Integer maxSize) {
        if (isEmpty(diningRoomName)) {
            diningRoomName = null;
        }
        if (null != diningRoomName && diningRoomName.length() > 10) {
            return defeat("就餐位名称不能超过10个字");
        }
        if (startIndex.intValue() < 0 || maxSize.intValue() <= 0) {
            return defeat("非法请求");
        }
        Boolean zf = (0 != zeroFlag.intValue() ? true : null);
        Boolean up = (0 != unPay.intValue() ? true : null);
        Boolean ff = (0 != finishFlag.intValue() ? true : null);
        return success(mBillService.getBillListWithCondition(diningRoomName, zf, up, ff, startIndex, maxSize));
    }

    /**
     * 获取指定账单下的所有的订单
     * @param billNumber
     * @return
     */
    @PostMapping("orderListInBill")
    public ResponseResult getOrderListWithBill(@RequestParam("billNumber")String billNumber) {
        if (isEmpty(billNumber)) {
            return defeat("非法操作");
        }
        return success(mBillService.getBillInfo(billNumber));
    }

    /**
     * 获取所有账单的统计信息
     * @return
     */
    @GetMapping("billCountInfo")
    public ResponseResult getBillCountInfo() {
        return success(mBillService.getBillCountInfo());
    }

}
