package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.CustomerMessageTypeDao;
import com.baldyoung.vita.common.pojo.entity.CustomerMessageTypeEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.CUSTOMER_MESSAGE_TYPE_NAME_EXISTS;

/**
 * 顾客消息类型服务
 */
@Service
public class MCustomerMessageTypeServiceImpl {
    @Autowired
    private CustomerMessageTypeDao customerMessageTypeDao;

    public List<CustomerMessageTypeEntity> getAllCustomerMessageType() {
        return customerMessageTypeDao.selectAll();
    }

    // 新增顾客消息类型
    public void addCustomerMessageType(String customerMessageTypeName) {
        CustomerMessageTypeEntity customerMessageTypeEntity = new CustomerMessageTypeEntity();
        customerMessageTypeEntity.setCustomerMessageTypeName(customerMessageTypeName);
        customerMessageTypeDao.insertCustomerMessageType(customerMessageTypeEntity);
    }
    // 修改顾客消息类型
    public void updateCustomerMessageType(Integer customerMessageTypeId, String customerMessageTypeName) {
        CustomerMessageTypeEntity customerMessageTypeEntity = new CustomerMessageTypeEntity();
        customerMessageTypeEntity.setCustomerMessageTypeName(customerMessageTypeName);
        customerMessageTypeEntity.setCustomerMessageTypeId(customerMessageTypeId);
        customerMessageTypeDao.updateCustomerMessageType(customerMessageTypeEntity);
    }

    // 删除指定顾客消息类型
    public void deleteCustomerMessageType(Integer customerMessageTypeId) {
        customerMessageTypeDao.delete(customerMessageTypeId);
    }

    // 新增或修改顾客消息类型
    public void addOrUpdateCustomerMessageType(Integer customerMessageTypeId, String customerMessageTypeName) throws ServiceException {
        CustomerMessageTypeEntity existsEntity = customerMessageTypeDao.selectByCustomerMessageTypeName(customerMessageTypeName);
        if (null != existsEntity && null != existsEntity.getCustomerMessageTypeId()) {
            // 名称已存在，抛出业务异常
            if (null == customerMessageTypeId || customerMessageTypeId.intValue() != existsEntity.getCustomerMessageTypeId().intValue()) {
                throw new ServiceException(CUSTOMER_MESSAGE_TYPE_NAME_EXISTS);
            }
        }
        if (null == customerMessageTypeId) {
            addCustomerMessageType(customerMessageTypeName);
        } else {
            updateCustomerMessageType(customerMessageTypeId, customerMessageTypeName);
        }

    }

}
