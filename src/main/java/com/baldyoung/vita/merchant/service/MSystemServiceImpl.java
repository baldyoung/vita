package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.CustomerMessageDao;
import com.baldyoung.vita.common.dao.DiningRoomReservationDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.pojo.dto.diningRoom.MDiningRoomNewsDto;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.CustomerMessageServiceImpl;
import com.baldyoung.vita.merchant.serverEndpoint.MerchantSystemMessageServerPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MSystemServiceImpl {

    @Autowired
    private CustomerMessageServiceImpl customerMessageService;

    @Autowired
    private DiningRoomReservationDao diningRoomReservationDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerMessageDao customerMessageDao;

    /**
     * 获取指定就餐位的客户消息
     * @param roomId
     * @return
     */
    public List<CustomerMessageEntity> getRoomMessage(Integer roomId) {
        return customerMessageService.getMessageListForRoomId(roomId);
    }

    /**
     * 修改指定客户消息的状态
     * @param recordId
     * @param newStatus
     * @throws ServiceException
     */
    public void updateMessageStatus(Integer recordId, Integer newStatus) throws ServiceException {
        customerMessageService.setMessageStatus(recordId, newStatus);
    }

    /**
     * 删除指定就餐位的所有客户消息
     * @param roomId
     */
    public void deleteRoomMessage(Integer roomId) {
        customerMessageService.deleteRoomMessage(roomId);
    }

    /**
     * 获取指定就餐位的所有预约记录
     * @param roomId
     * @return
     */
    public List<DiningRoomReservationEntity> getReservationOfRoom(Integer roomId) {
        return diningRoomReservationDao.selectByRoomId(roomId);
    }

    /**
     * 新增一条预约记录
     * @param entity
     */
    public void addReservationForRoom(DiningRoomReservationEntity entity) {
        entity.setReservationStatus(0);
        diningRoomReservationDao.insertReservationEntity(entity);
    }

    /**
     * 修改指定的预约记录
     * @param entity
     */
    public void updateReservation(DiningRoomReservationEntity entity) {
        diningRoomReservationDao.updateReservationEntity(entity);
    }

    /**
     * 删除指定预约记录
     * @param recordId
     */
    public void deleteReservation(Integer recordId) {
        diningRoomReservationDao.deleteReservationEntity(recordId);
    }

    /**
     * 将指定记录标识到对应的就餐位上
     * @param recordId
     */
    public void setReservationOnTip(Integer recordId) {
        DiningRoomReservationEntity entity = diningRoomReservationDao.selectByRecordId(recordId);
        if (null == entity || null == entity.getReservationId()) {
            return;
        }
        // 清除原先的记录标识
        diningRoomReservationDao.updateReservationOffTipStatus(entity.getDiningRoomId());
        DiningRoomReservationEntity newEntity = new DiningRoomReservationEntity();
        newEntity.setReservationId(entity.getReservationId());
        newEntity.setReservationStatus(1);
        diningRoomReservationDao.updateReservationEntity(newEntity);
    }

    /**
     * 移除指定就餐位的预约标识
     * @param roomId
     */
    public void removeRoomReservationTip(Integer roomId) {
        diningRoomReservationDao.updateReservationOffTipStatus(roomId);
    }

    /**
     * 获取最新系统消息（新订单项、新客户消息）
     * @return
     */
    public List<MDiningRoomNewsDto> getCurrentDiningRoomNews() {
        Map<Integer, Integer> orderItemMap = new HashMap();
        Map<Integer, Integer> messageMap = new HashMap();
        Map<Integer, MDiningRoomNewsDto> newsMap = new HashMap();
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderProductItemStatusFlag(0);
        List<OrderItemEntity> orderItemEntityList = orderItemDao.selectOrderItemListWithCondition(orderItemEntity);
        for (OrderItemEntity entity : orderItemEntityList) {
            Integer roomId = entity.getOwnerId();
            Integer currentNumber = orderItemMap.get(roomId);
            if (null == currentNumber) {
                orderItemMap.put(roomId, 1);
                continue;
            }
            orderItemMap.put(roomId, currentNumber + 1);
        }
        CustomerMessageEntity messageEntity = new CustomerMessageEntity();
        messageEntity.setCustomerMessageStatus(0);
        List<CustomerMessageEntity> messageEntityList = customerMessageDao.selectWithCondition(messageEntity);
        for (CustomerMessageEntity entity : messageEntityList) {
            Integer roomId = entity.getDiningRoomId();
            Integer currentNumber = messageMap.get(roomId);
            if (null == currentNumber) {
                messageMap.put(roomId, 1);
                continue;
            }
            messageMap.put(roomId, currentNumber + 1);
        }
        for (Map.Entry<Integer, Integer> entry : orderItemMap.entrySet()) {
            MDiningRoomNewsDto dto = new MDiningRoomNewsDto();
            dto.setRoomId(entry.getKey());
            dto.setOrderNewsNumber(entry.getValue());
            newsMap.put(entry.getKey(), dto);
        }
        for (Map.Entry<Integer, Integer> entry : messageMap.entrySet()) {
            MDiningRoomNewsDto dto = newsMap.get(entry.getKey());
            if (null == dto) {
                dto = new MDiningRoomNewsDto();
                dto.setRoomId(entry.getKey());
            }
            dto.setCustomerMessageNewsNumber(entry.getValue());
            newsMap.put(entry.getKey(), dto);
        }
        return new LinkedList(newsMap.values());
    }

    @Scheduled(fixedRate=2000)
    public void newsOption() {
        MerchantSystemMessageServerPoint.newsOption(getCurrentDiningRoomNews());
    }

}
