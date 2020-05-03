package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.orderItem.OrderItemReceiveDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static java.lang.System.out;

@RestController
@RequestMapping("order")
public class COrderController {


    @PostMapping("do")
    public ResponseResult receiveOrder(@RequestBody List<OrderItemReceiveDto> itemList) {
        out.println("新订单数据");
        out.println(itemList);
        return success();
    }
}
