package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.merchant.service.MDiningRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

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

    /**
     * 获取所有的就餐位信息（不包含预定数据）
     * @return
     */
    @GetMapping("diningRoomList")
    public ResponseResult getDiningRoomList() {
        return success(mDiningRoomService.getDiningRoomList());
    }

    /**
     * 新增一个就餐位
     * @param roomName
     * @param roomInfo
     * @param roomGrade
     * @return
     */
    @PostMapping("addDiningRoom")
    public ResponseResult addDiningRoom(@RequestParam("roomName")String roomName,
                                        @RequestParam(value = "roomInfo", required = false)String roomInfo,
                                        @RequestParam(value = "roomGrade", required = false)Integer roomGrade) {
        if (isEmpty(roomGrade)) {
            return defeat("就餐位名称不能为空");
        }
        if (isEmpty(roomInfo)) {
            roomInfo = " ";
        }
        if (null == roomGrade) {
            roomGrade = 0;
        }
        if (roomName.length() > 10) {
            return defeat("名称不能超过10个字");
        }
        if (roomInfo.length() > 150) {
            return defeat("名称不能超过150个字");
        }
        mDiningRoomService.addDiningRoom(roomName, roomGrade, roomInfo);
        return success();
    }

    /**
     * 删除指定就餐位
     * @param roomId
     * @return
     */
    @PostMapping("deleteDiningRoom")
    public ResponseResult deleteDiningRoom(@RequestParam("roomId")Integer roomId) {
        mDiningRoomService.deleteDiningRoom(roomId);
        return success();
    }

    /**
     * 修改指定就餐位数据
     * @param roomId
     * @param roomName
     * @param roomInfo
     * @param roomGrade
     * @return
     */
    @PostMapping("updateDiningRoom")
    public ResponseResult updateDiningRoom(@RequestParam("roomId")Integer roomId,
                                           @RequestParam("roomName")String roomName,
                                           @RequestParam("roomInfo")String roomInfo,
                                           @RequestParam("roomGrade")Integer roomGrade) {
        DiningRoomEntity entity = new DiningRoomEntity();
        entity.setDiningRoomId(roomId);
        entity.setDiningRoomId(roomGrade);
        entity.setDiningRoomName(roomName);
        entity.setDiningRoomGrade(roomGrade);
        entity.setDiningRoomInfo(roomInfo);
        mDiningRoomService.updateDiningRoom(entity);
        return success();
    }

}
