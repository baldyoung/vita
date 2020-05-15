package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.merchant.service.MDiningRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("changeStatus")
    public ResponseResult setDiningRoomStatus(@RequestParam("roomId")Integer roomId,
                                              @RequestParam("newStatusId")Integer statusId) {
        DiningRoomEntity entity = new DiningRoomEntity();
        entity.setDiningRoomId(roomId);
        entity.setDiningRoomStatus(statusId);
        mDiningRoomService.updateDiningRoom(entity);
        return success();
    }

}
