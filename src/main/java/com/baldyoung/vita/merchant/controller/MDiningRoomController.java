package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.merchant.service.MDiningRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("mDiningRoom")
public class MDiningRoomController {

    @Autowired
    private MDiningRoomServiceImpl mDiningRoomService;

    @GetMapping("list")
    public ResponseResult getAllDiningRoomInfo() {
        return success(mDiningRoomService.getAllDiningRoom());
    }


}
