package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.dao.*;
import com.baldyoung.vita.common.pojo.dto.bill.CBillDto;
import com.baldyoung.vita.common.pojo.dto.message.CMessageTypeDto;
import com.baldyoung.vita.common.pojo.dto.order.COrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.COrderItemDto;
import com.baldyoung.vita.common.pojo.entity.*;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import com.baldyoung.vita.common.service.impl.CustomerMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.NO_ORDER;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;
import static com.baldyoung.vita.common.utility.CommonMethod.toCOrderItemDto;

@Service
public class CSystemServiceImpl {
    @Autowired
    private CustomerMessageServiceImpl customerMessageService;

    @Autowired
    private CompletedBillDao completedBillDao;

    @Autowired
    private CompletedOrderDao completedOrderDao;

    @Autowired
    private CompletedOrderItemDao completedOrderItemDao;

    @Autowired
    private DiningRoomDao diningRoomDao;

    /**
     * 获取所有的服务类型数据
     * @return
     */
    public List<CMessageTypeDto> getAllType() {
        List<CustomerMessageTypeEntity> typeList = customerMessageService.getAllMessageType();
        List<CMessageTypeDto> list = new ArrayList(typeList.size());
        CMessageTypeDto dto;
        for (CustomerMessageTypeEntity entity : typeList) {
            dto = new CMessageTypeDto();
            dto.setCustomerMessageTypeId(entity.getCustomerMessageTypeId());
            dto.setCustomerMessageTypeName(entity.getCustomerMessageTypeName());
            list.add(dto);
        }
        return list;
    }

    /**
     * 发送服务消息
     * @param roomId
     * @param typeId
     * @param messageContent
     */
    public void sendMessage(Integer roomId, Integer typeId, String messageContent) {
        customerMessageService.sendMessageForRoom(roomId, typeId, messageContent);
    }


    /**
     * 获取指定账单的信息及其所有订单集合
     * @param billNumber
     * @return
     * @throws ServiceException
     */
    public CBillDto getCompletedBillInfo(String  billNumber) throws ServiceException {
        BillEntity billEntity = completedBillDao.selectBill(billNumber);
        CBillDto dto = new CBillDto();
        dto.setBillCustomerNumber(billEntity.getBillCustomerNumber());
        dto.setBillEndDateTime(billEntity.getBillEndDateTime());
        dto.setBillNumber(billEntity.getBillNumber());
        dto.setBillOrderQuantity(billEntity.getBillOrderQuantity());
        dto.setBillOwnerId(billEntity.getBillOwnerId());
        dto.setBillOwnerName(billEntity.getBillOwnerName());
        dto.setBillOwnerTypeFlag(billEntity.getBillOwnerTypeFlag());
        dto.setBillReceivedAmount(billEntity.getBillReceivedAmount());
        dto.setBillReceivedDateTime(billEntity.getBillReceivedDateTime());
        dto.setBillRecentHandlerId(billEntity.getBillRecentHandlerId());
        dto.setBillRecentHandlerName(billEntity.getBillRecentHandlerName());
        dto.setBillStartDateTime(billEntity.getBillStartDateTime());
        dto.setBillTotalAmount(billEntity.getBillTotalAmount());
        dto.setBillCustomerName(billEntity.getBillCustomerName());

        // 获取订单详情
        List<OrderEntity> orderList = completedOrderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(orderList)) {
            throw new ServiceException(NO_ORDER);
        }
        List<COrderDto> cOrderDtoList = new ArrayList(orderList.size());
        List<Integer> orderIds = orderList.stream().map(cell->cell.getOrderId()).collect(Collectors.toList());
        List<OrderItemEntity>  orderItemList = completedOrderItemDao.selectOrderItemListWithOrderIdList(orderIds);
        DiningRoomEntity diningRoomEntity = diningRoomDao.selectByDiningRoomId(billEntity.getBillOwnerId());
        for (OrderEntity order : orderList) {
            COrderDto cOrderDto = new COrderDto();
            cOrderDto.setOrderId(order.getOrderId());
            cOrderDto.setBillNumber(order.getBillNumber());
            cOrderDto.setOrderCreateDateTime(order.getOrderCreateDateTime());
            cOrderDto.setOrderPresetTime(order.getOrderPresetTime());
            cOrderDto.setOrderTypeFlag(order.getOrderTypeFlag());
            cOrderDto.setOwnerName(diningRoomEntity.getDiningRoomName());
            List<COrderItemDto> cOrderItemDtoList = new ArrayList(orderItemList.size());
            for (OrderItemEntity item : orderItemList) {
                if (item.getOrderId().equals(order.getOrderId())) {
                    cOrderItemDtoList.add(toCOrderItemDto(item));
                }
            }
            cOrderDto.setItemList(cOrderItemDtoList);
            cOrderDtoList.add(cOrderDto);
        }

        dto.setOrderList(cOrderDtoList);
        return dto;
    }


}
