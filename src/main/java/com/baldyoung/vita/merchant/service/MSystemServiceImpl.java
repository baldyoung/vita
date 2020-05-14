package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.pojo.entity.CustomerMessageEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.CustomerMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSystemServiceImpl {

    @Autowired
    private CustomerMessageServiceImpl customerMessageService;

    public List<CustomerMessageEntity> getRoomMessage(Integer roomId) {
        return customerMessageService.getMessageListForRoomId(roomId);
    }

    public void updateMessageStatus(Integer recordId, Integer newStatus) throws ServiceException {
        customerMessageService.setMessageStatus(recordId, newStatus);
    }

    public void deleteRoomMessage(Integer roomId) {
        customerMessageService.deleteRoomMessage(roomId);
    }

}
