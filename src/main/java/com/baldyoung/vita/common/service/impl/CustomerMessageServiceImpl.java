package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.dao.CustomerMessageDao;
import com.baldyoung.vita.common.dao.CustomerMessageTypeDao;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageEntity;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageTypeEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.MESSAGE_NO_FOUND;

@Service
public class CustomerMessageServiceImpl {
    @Autowired
    private CustomerMessageDao customerMessageDao;

    @Autowired
    private CustomerMessageTypeDao customerMessageTypeDao;

    @Autowired
    private SystemMessageServiceImpl systemMessageService;

    /**
     * 获取所有的消息类型
     * @return
     */
    public List<CustomerMessageTypeEntity> getAllMessageType() {
        return customerMessageTypeDao.selectAll();
    }

    /**
     * 获取指定就餐位的所有消息
     * @param roomId
     * @return
     */
    public List<CustomerMessageEntity> getMessageListForRoomId(Integer roomId) {
        return customerMessageDao.selectByDiningRoomId(roomId);
    }

    /**
     * 发送系统消息到指定就餐位
     * @param roomId
     * @param messageTypeId
     * @param messageData
     */
    public void sendMessageForRoom(Integer roomId, Integer messageTypeId, String messageData) {
        CustomerMessageTypeEntity typeEntity = customerMessageTypeDao.selectByCustomerMessageTypeId(messageTypeId);
        if (null == typeEntity || null == typeEntity.getCustomerMessageTypeId()) {
            return ;
        }
        CustomerMessageEntity entity = new CustomerMessageEntity();
        entity.setCustomerMessageStatus(0);
        entity.setDiningRoomId(roomId);
        entity.setCustomerMessageTypeId(messageTypeId);
        entity.setCustomerMessageTypeName(typeEntity.getCustomerMessageTypeName());
        entity.setCustomerMessageValue(messageData);
        customerMessageDao.insert(entity);
        systemMessageService.pullMerchantUnreadMessage();
    }

    /**
     * 修改消息状态标识
     * @param recordId
     * @param newStatus
     * @throws ServiceException
     */
    public void setMessageStatus(Integer recordId, Integer newStatus) throws ServiceException {
        CustomerMessageEntity entity = customerMessageDao.selectByRecordId(recordId);
        if (null == entity || null == entity.getCustomerMessageId()) {
            throw new ServiceException(MESSAGE_NO_FOUND);
        }
        entity.setCustomerMessageStatus(newStatus);
        customerMessageDao.update(entity);
        systemMessageService.pullMerchantUnreadMessage();
    }

    /**
     * 删除指定就餐位下的所有消息
     * @param roomId
     */
    public void deleteRoomMessage(Integer roomId) {
        customerMessageDao.deleteRecordForRoomId(roomId);
        systemMessageService.pullMerchantUnreadMessage();
    }



}
