package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.*;
import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import com.baldyoung.vita.common.service.impl.OrderServiceImpl;
import com.baldyoung.vita.common.service.impl.SystemMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class MCompletedOrderServiceImpl {


    @Autowired
    private CompletedOrderDao completedOrderDao;

    @Autowired
    private CompletedOrderItemDao completedOrderItemDao;


    @PostConstruct
    public void init() {
    }

    /**
     * 获取指定账单下的所有订单
     * @param billNumber
     * @return
     */
    public List<MOrderDto> getAllOrderInBill(String billNumber) {
        List<OrderEntity> entityList = completedOrderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(entityList)) {
            return new ArrayList(0);
        }
        List<Integer> orderIds = new ArrayList(entityList.size());
        for (OrderEntity entity : entityList) {
            orderIds.add(entity.getOrderId());
        }
        List<MOrderDto> result = new ArrayList(entityList.size());
        List<OrderItemEntity> itemEntityList = completedOrderItemDao.selectOrderItemListWithOrderIdList(orderIds);
        for (OrderEntity entity : entityList) {
            MOrderDto dto = new MOrderDto();
            Integer orderId = entity.getOrderId();
            dto.setBillNumber(entity.getBillNumber());
            dto.setOrderCreateDateTime(entity.getOrderCreateDateTime());
            dto.setOrderId(entity.getOrderId());
            dto.setOrderInitiatorFlag(entity.getOrderInitiatorFlag());
            dto.setOrderPresetTime(entity.getOrderPresetTime());
            dto.setOrderTypeFlag(entity.getOrderTypeFlag());
            List<MOrderItemDto> itemList = new LinkedList();
            for (OrderItemEntity itemEntity : itemEntityList) {
                Integer itemOrderId = itemEntity.getOrderId();
                if (itemOrderId.equals(orderId)) {
                    MOrderItemDto itemDto = new MOrderItemDto();
                    itemDto.setOrderProductId(itemEntity.getOrderProductId());
                    itemDto.setOrderProductImg(itemEntity.getOrderProductImg());
                    itemDto.setOrderProductItemId(itemEntity.getOrderProductItemId());
                    itemDto.setOrderProductItemStatusDesc(itemEntity.getOrderProductItemStatusDesc());
                    itemDto.setOrderProductItemStatusFlag(itemEntity.getOrderProductItemStatusFlag());
                    itemDto.setOrderProductName(itemEntity.getOrderProductName());
                    itemDto.setOrderProductPrice(itemEntity.getOrderProductPrice());
                    itemDto.setOrderProductQuantity(itemEntity.getOrderProductQuantity());
                    itemDto.setOrderProductRemarks(itemEntity.getOrderProductRemarks());
                    itemList.add(itemDto);
                }
            }
            dto.setItemList(itemList);
            result.add(dto);
        }
        return result;
    }


    /**
     * 获取指定订单的数据
     * 订单信息 + 相应商品项
     * @param orderId
     * @return
     * @throws ServiceException
     */
    public MOrderDto getOrderInfo(Integer orderId) throws ServiceException {
        OrderEntity orderEntity = completedOrderDao.selectOrderByOrderId(orderId);
        if (null == orderEntity || null == orderEntity.getOrderId()) {
            throw new ServiceException(ORDER_NOT_EXISTS);
        }
        MOrderDto mOrderDto = new MOrderDto();
        mOrderDto.setOrderTypeFlag(orderEntity.getOrderTypeFlag());
        mOrderDto.setOrderPresetTime(orderEntity.getOrderPresetTime());
        mOrderDto.setOrderInitiatorFlag(orderEntity.getOrderInitiatorFlag());
        mOrderDto.setOrderCreateDateTime(orderEntity.getOrderCreateDateTime());
        List<OrderItemEntity> orderItemEntityList = completedOrderItemDao.selectOrderItemList(orderId);
        if (isEmptyCollection(orderItemEntityList)) {
            return mOrderDto;
        }
        List<MOrderItemDto> orderItemList = new ArrayList(orderItemEntityList.size());
        for (OrderItemEntity item : orderItemEntityList) {
            MOrderItemDto mItem = new MOrderItemDto();
            mItem.setOrderProductName(item.getOrderProductName());
            mItem.setOrderProductImg(item.getOrderProductImg());
            mItem.setOrderProductPrice(item.getOrderProductPrice());
            mItem.setOrderProductQuantity(item.getOrderProductQuantity());
            mItem.setOrderProductItemStatusFlag(item.getOrderProductItemStatusFlag());
            mItem.setOrderProductItemStatusDesc(item.getOrderProductItemStatusDesc());
            mItem.setOrderProductRemarks(item.getOrderProductRemarks());
            orderItemList.add(mItem);
        }
        mOrderDto.setItemList(orderItemList);
        return mOrderDto;
    }

}
