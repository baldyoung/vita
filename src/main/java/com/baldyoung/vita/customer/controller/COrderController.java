package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.orderItem.OrderItemReceiveDto;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
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

    @PostMapping("do")
    public ResponseResult receiveOrder(@RequestBody List<OrderItemReceiveDto> itemList) {
        out.println("新订单数据");
        out.println(itemList);
        return success();
    }

    @GetMapping("diningData")
    public ResponseResult getDiningData(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cShoppingCartService.getDiningData(roomId));
    }
}
