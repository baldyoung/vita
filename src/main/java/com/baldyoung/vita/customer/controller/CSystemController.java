package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.service.CSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

}
