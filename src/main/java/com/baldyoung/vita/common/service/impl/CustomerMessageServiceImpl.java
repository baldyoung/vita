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


    public List<CustomerMessageTypeEntity> getAllMessageType() {
        return customerMessageTypeDao.selectAll();
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
        entity.setCustomerMessageTypeId(messageTypeId);
        entity.setCustomerMessageTypeName(typeEntity.getCustomerMessageTypeName());
        entity.setCustomerMessageValue(messageData);
        customerMessageDao.insert(entity);
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
    }




}
