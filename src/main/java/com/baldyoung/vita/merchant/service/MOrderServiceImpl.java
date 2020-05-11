package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class MOrderServiceImpl {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BillServiceImpl billService;

    /**
     * 获取指定就餐位的所有订单
     * @param billNumber
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(String billNumber) {
        List<OrderEntity> entityList = orderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(entityList)) {
            return new ArrayList(0);
        }
        List<Integer> orderIds = new ArrayList(entityList.size());
        for (OrderEntity entity : entityList) {
            orderIds.add(entity.getOrderId());
        }
        List<MOrderDto> result = new ArrayList(entityList.size());
        List<OrderItemEntity> itemEntityList = orderItemDao.selectOrderItemListWithOrderIdList(orderIds);
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
     * 获取指定就餐位的所有订单
     * @param roomId
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(Integer roomId) {
        String billNumber = billService.getRoomBillNumber(roomId);
        return getAllOrderInRoom(billNumber);
    }

}
