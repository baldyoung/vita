package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("mSystem")
public class MSystemController {
    @Autowired
    private MSystemServiceImpl mSystemService;

    /**
     * 获取指定就餐位的所有消息
     * @param roomId
     * @return
     */
    @GetMapping("message")
    public ResponseResult getRoomMessage(@RequestParam("roomId")Integer roomId) {
        return success(mSystemService.getRoomMessage(roomId));
    }

    /**
     * 更新指定消息的状态
     * @param recordId
     * @param status
     * @return
     * @throws ServiceException
     */
    @PostMapping("set")
    public ResponseResult setRecordStatus(@RequestParam("recordId")Integer recordId,
                                          @RequestParam("status")Integer status) throws ServiceException {
        mSystemService.updateMessageStatus(recordId, status);
        return success();
    }

    /**
     * 删除指定就餐位的所有消息
     * @param roomId
     * @return
     */
    @PostMapping("delete")
    public ResponseResult deleteRoomMessage(@RequestParam("roomId")Integer roomId) {
        mSystemService.deleteRoomMessage(roomId);
        return success();
    }
}
