package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.orderItem.OrderItemReceiveDto;
import com.baldyoung.vita.common.pojo.dto.shoppingCart.DiningData;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.service.COrderServiceImpl;
import com.baldyoung.vita.customer.service.CShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.getRoomIdFromSession;
import static java.lang.System.out;

@RestController
@RequestMapping("order")
public class COrderController {

    @Autowired
    private CShoppingCartServiceImpl cShoppingCartService;

    @Autowired
    private COrderServiceImpl cOrderService;

    @PostMapping("do")
    public ResponseResult receiveOrder(@RequestBody List<OrderItemReceiveDto> itemList, HttpSession session) throws UtilityException, ServiceException {
        //out.println("新订单数据");
        //out.println(itemList);
        Integer roomId = getRoomIdFromSession(session);
        DiningData diningData = cShoppingCartService.getDiningData(roomId);
        return success(cOrderService.doOrder(roomId, diningData.getDiningType(), diningData.getDiningTime(), itemList));
    }

    @GetMapping("diningData")
    public ResponseResult getDiningData(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cShoppingCartService.getDiningData(roomId));
    }

    @GetMapping("advanceOrder")
    public ResponseResult getAdvanceOrder(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cOrderService.getAdvanceOrder(roomId));
    }

    @GetMapping("lastOrder")
    public ResponseResult getLastOrder(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cOrderService.getLastOrder(roomId));
    }

    @GetMapping("orderList")
    public ResponseResult getOrderList(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cOrderService.getOrderList(roomId));
    }

    @GetMapping("diningRoomInfo")
    public ResponseResult getDiningRoomInfo(HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cOrderService.getDiningRoomInfo(roomId));
    }


}
