package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("mBill")
public class MBillController {

    @Autowired
    private MBillServiceImpl mBillService;

    @GetMapping("billInfo")
    public ResponseResult getBillInfo(@RequestParam("roomId")Integer roomId) throws ServiceException {
        return success(mBillService.getBillInfo(roomId));
    }
}
