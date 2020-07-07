package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.merchant.service.MOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;

@RestController
@RequestMapping("mOrder")
public class MOrderController {

    @Autowired
    private MOrderServiceImpl mOrderService;

    /**
     * 设置订单项为下单成功，等待商家确认
     * @param itemId
     * @return
     */
    @PostMapping("unRead")
    public ResponseResult setOrderItemUnRead(@RequestParam("itemId")Integer itemId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductItemStatusFlag(0);
        mOrderService.updateOrderItem(entity);
        return success();
    }

    /**
     * 设置订单项为下单失败，库存不足
     * @param itemId
     * @return
     */
    @PostMapping("failed")
    public ResponseResult setOrderItemFailed(@RequestParam("itemId")Integer itemId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductItemStatusFlag(1);
        mOrderService.updateOrderItem(entity);
        return success();
    }

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
        entity.setOrderProductQuantity(0);
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

    /**
     * 新增订单项
     * @param roomId
     * @param productId
     * @param productName
     * @param productPrice
     * @param productQuantity
     * @param diningType
     * @return
     */
    @PostMapping("addItem")
    public ResponseResult addOrderItem(@RequestParam("roomId")Integer roomId,
                                       @RequestParam(value = "productId", required = false)Integer productId,
                                       @RequestParam("productName")String productName,
                                       @RequestParam("productPrice")BigDecimal productPrice,
                                       @RequestParam("productQuantity")Integer productQuantity,
                                       @RequestParam("diningType")Integer diningType,
                                       @RequestParam(value = "productImgName", required = false)String productImgName,
                                       @RequestParam(value = "productRemarks", required = false)String productRemarks) throws ServiceException {
        if (isEmpty(productName)) {
            return defeat("商品名称不能为空");
        }
        if (!isEmpty(productImgName) && productImgName.length() >= 100) {
            return defeat("非法数据");
        }
        if (!isEmpty(productRemarks) && productRemarks.length() >= 40) {
            return defeat("口味字数需小于40");
        }
        if (productName.length() >= 60) {
            return defeat("商品名称需小于60");
        }
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductId(productId);
        entity.setOrderProductName(productName);
        entity.setOrderProductPrice(productPrice);
        entity.setOrderProductQuantity(productQuantity);
        entity.setOrderProductImg(productImgName);
        entity.setOrderProductRemarks(productRemarks);
        mOrderService.addOrderItem(roomId, diningType, entity);
        return success();
    }

    /**
     * 获取指定订单的信息
     * @param orderId
     * @return
     * @throws ServiceException
     */
    @GetMapping("orderInfo")
    public ResponseResult getOrderInfo(@RequestParam("orderId")Integer orderId) throws ServiceException {

        return success(mOrderService.getOrderInfo(orderId));
    }

}
