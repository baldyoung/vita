package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.DiningRoomReservationDao;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomReservationEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.CustomerMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSystemServiceImpl {

    @Autowired
    private CustomerMessageServiceImpl customerMessageService;

    @Autowired
    private DiningRoomReservationDao diningRoomReservationDao;

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

}
