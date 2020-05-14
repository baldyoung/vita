package com.baldyoung.vita.customer.service;

import com.baldyoung.vita.common.pojo.dto.message.CMessageTypeDto;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageTypeEntity;
import com.baldyoung.vita.common.service.impl.CustomerMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CSystemServiceImpl {
    @Autowired
    private CustomerMessageServiceImpl customerMessageService;

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


}
