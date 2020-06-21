package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.service.CSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.getRoomIdFromSession;

@RestController
@RequestMapping("system")
public class CSystemController {
    @Autowired
    private CSystemServiceImpl cSystemService;


    @GetMapping("messageType")
    public ResponseResult getMessageTypeList(HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cSystemService.getAllType());
    }

    @PostMapping("sendMessage")
    public ResponseResult sendMessage(
            @RequestParam("messageTypeId")Integer typeId,
            HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        cSystemService.sendMessage(roomId, typeId, "");
        return success();
    }

    /**
     * 获取电子账单的数据
     * @param session
     * @return
     * @throws ServiceException
     */
    @GetMapping("invoiceInfo")
    public ResponseResult getInvoiceInfo(HttpSession session) throws ServiceException {
        Object object = session.getAttribute("billNumber");
        if (null == object) {
            return defeat();
        }
        return success(cSystemService.getBillInfo(String.valueOf(object)));
    }



}
