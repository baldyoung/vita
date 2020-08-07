package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.common.service.impl.DiningRoomRequestPositionServiceImpl;
import com.baldyoung.vita.common.utility.CommonMethod;
import com.baldyoung.vita.merchant.serverEndpoint.MerchantSystemMessageServerPoint;
import com.baldyoung.vita.merchant.service.MProductStockServiceImpl;
import com.baldyoung.vita.merchant.service.MSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
import static com.baldyoung.vita.common.utility.CommonMethod.getMerchantUserIdFromSession;
import static com.baldyoung.vita.common.utility.CommonMethod.isAnyEmpty;

@RestController
@RequestMapping("mSystem")
public class MSystemController {
    @Autowired
    private MSystemServiceImpl mSystemService;

    @Autowired
    private DiningRoomRequestPositionServiceImpl diningRoomRequestPositionService;

    @Autowired
    private MProductStockServiceImpl mProductStockService;

    @Autowired
    private CommonConfig commonConfig;

    @GetMapping("resetAllProductStock")
    public ResponseResult resetAllProductStock() throws ServiceException {
        mProductStockService.productStockReset();
        return success();
    }

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

    /**
     * 获取指定就餐位的预约记录
     * @param roomId
     * @return
     */
    @GetMapping("reservationList")
    public ResponseResult getReservationOfRoom(@RequestParam("roomId")Integer roomId) {
        return success(mSystemService.getReservationOfRoom(roomId));
    }

    /**
     * 新增预约记录
     * @param roomId
     * @param customerName
     * @param reservationInfo
     * @param diningDate
     * @param diningTime
     * @return
     */
    @PostMapping("addReservation")
    public ResponseResult addReservationOfRoom(@RequestParam("roomId")Integer roomId,
                                               @RequestParam("customerName")String customerName,
                                               @RequestParam("reservationInfo")String reservationInfo,
                                               @RequestParam("diningDate")String diningDate,
                                               @RequestParam("diningTime")String diningTime) {
        if (isAnyEmpty(customerName, diningDate, diningTime) || null == reservationInfo) {
            return defeat("请补全预约信息");
        }
        if (customerName.length() >= 30) {
            return defeat("客户名称要少于30个字");
        }
        if (reservationInfo.length() >= 200) {
            return defeat("预约备注要少于200个字");
        }
        if (diningDate.length() >= 15) {
            return defeat("就餐日期要少于15个字");
        }
        if (diningTime.length() >= 5) {
            return defeat("就餐时间要少于5个字");
        }
        DiningRoomReservationEntity entity = new DiningRoomReservationEntity();
        entity.setDiningRoomId(roomId);
        entity.setCustomerName(customerName);
        entity.setReservationInfo(reservationInfo);
        entity.setDiningDate(diningDate);
        entity.setDiningTime(diningTime);
        mSystemService.addReservationForRoom(entity);
        return success();
    }

    /**
     * 删除指定预约记录
     * @param recordId
     * @return
     */
    @PostMapping("reservationRemove")
    public ResponseResult deleteReservation(@RequestParam("recordId")Integer recordId) {
        mSystemService.deleteReservation(recordId);
        return success();
    }

    /**
     * 将指定预约记录标识到就餐位
     * @param recordId
     * @return
     */
    @PostMapping("setOnTip")
    public ResponseResult setReservationOnTip(@RequestParam("recordId")Integer recordId) {
        mSystemService.setReservationOnTip(recordId);
        return success();
    }

    /**
     * 移除指定就餐位的预约标识
     * @param roomId
     * @return
     */
    @PostMapping("removeRoomReservationTip")
    public ResponseResult removeRoomReservationTip(@RequestParam("roomId")Integer roomId) {
        mSystemService.removeRoomReservationTip(roomId);
        return success();
    }

    /**
     * 获取当前的系统未读数据
     * @return
     */
    @GetMapping("currentNews")
    public ResponseResult getCurrentNews() {
        return success(mSystemService.getCurrentDiningRoomNews());
    }

    /**
     * 获取指定就餐位的点餐二维码
     * @param roomId
     * @return
     */
    @GetMapping("positionQRcode")
    public ResponseResult getPositionQRcode(@RequestParam("roomId")Integer roomId) {
        String qrcodeImgName = diningRoomRequestPositionService.getDiningRoomKey(roomId) + ".jpg";
        return success(qrcodeImgName);
    }

    /**
     * 刷新指定就餐位的点餐二维码
     * @param roomId
     * @return
     * @throws Exception
     */
    @PostMapping("refreshPositionQRcode")
    public ResponseResult refreshPositionQRcode(@RequestParam("roomId")Integer roomId) throws Exception {
        diningRoomRequestPositionService.createNewPosition(roomId);
        return success();
    }

    @GetMapping("log")
    public ResponseResult getDebugLogData(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws IOException {
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        OutputStream outputStream = httpServletResponse.getOutputStream();

        if (null == outputStream) {
            return defeat("something wrong");
        }
        CommonMethod.readFileAndSendData(outputStream, commonConfig.debugLogFilePathName);
        outputStream.close();
        return null;
    }

    /**
     * 获取建立websocket所需要的安全密钥
     * @param session
     * @return
     * @throws UtilityException
     */
    @GetMapping("mSystemMessageKey")
    public ResponseResult getMSystemMessageKey(HttpSession session) throws UtilityException {
        Integer userId = getMerchantUserIdFromSession(session);
        String key = MerchantSystemMessageServerPoint.createLinkKey(userId);
        return success(key);
    }


}
