package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("mOrder")
public class MOrderController {

    @Autowired
    private MOrderServiceImpl mOrderService;

    /**
     * 设置订单项已读
     * @param itemId
     * @return
     */
    @PostMapping("read")
    public ResponseResult setOrderItemRead(@RequestParam("itemId")Integer itemId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductItemStatusFlag(2);
        mOrderService.updateOrderItem(entity);
        return success();
    }
    //public ResponseResult setOrderItemRead(@RequestParam("itemId"))

    /**
     * 设置订单项已完成
     * @param itemId
     * @return
     */
    @PostMapping("finish")
    public ResponseResult setOrderItemFinish(@RequestParam("itemId")Integer itemId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductItemStatusFlag(3);
        mOrderService.updateOrderItem(entity);
        return success();
    }


    /**
     * 删除订单项
     * @param itemId
     * @return
     */
    @PostMapping("delete")
    public ResponseResult setOrderItemDelete(@RequestParam("itemId")Integer itemId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductItemStatusFlag(4);
        mOrderService.updateOrderItem(entity);
        return success();
    }

    /**
     * 修改订单项单价和数量
     * @param itemId
     * @param price
     * @param quantity
     * @return
     */
    @PostMapping("update")
    public ResponseResult updateOrderItemInfo(@RequestParam("itemId")Integer itemId,
                                              @RequestParam(value = "itemProductPrice", required = false)BigDecimal price,
                                              @RequestParam(value = "itemProductQuantity", required = false)Integer quantity) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductPrice(price);
        entity.setOrderProductQuantity(quantity);
        mOrderService.updateOrderItem(entity);
        return success();
    }

    /**
     * 替换订单项的商品
     * @param itemId
     * @param productId
     * @return
     */
    @PostMapping("change")
    public ResponseResult exchangeOrderItemProduct(@RequestParam("itemId")Integer itemId,
                                                   @RequestParam("newProductId")Integer productId) throws ServiceException {
        mOrderService.changeOrderItemProduct(itemId, productId);
        return success();
    }

}
