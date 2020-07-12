package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.utility.CommonMethod;
import com.baldyoung.vita.merchant.service.MCustomerMessageTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.defeat;
import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("mCustomerMessageType")
public class MCustomerMessageTypeController {

    @Autowired
    private MCustomerMessageTypeServiceImpl mCustomerMessageTypeService;

    /**
     * 获取所有的顾客消息类型
     * @return
     */
    @GetMapping("list")
    public ResponseResult getCustomerMessageTypeList() {
        return success(mCustomerMessageTypeService.getAllCustomerMessageType());
    }

    /**
     * 新增或修改顾客消息类型
     * @param customerMessageTypeId
     * @param customerMessageTypeName
     * @return
     */
    @PostMapping("addOrUpdate")
    public ResponseResult addCustomerMessageType(
            @RequestParam(value = "customerMessageTypeId", required = false)Integer customerMessageTypeId,
            @RequestParam("customerMessageTypeName")String customerMessageTypeName) throws ServiceException {
        if (CommonMethod.isEmpty(customerMessageTypeName)) {
            return defeat("名称不能为空");
        }
        if (customerMessageTypeName.length() >= 20) {
            return defeat("名称长度要小于20");
        }
        mCustomerMessageTypeService.addOrUpdateCustomerMessageType(customerMessageTypeId, customerMessageTypeName);
        return success();
    }

    /**
     * 删除指定的顾客消息类型
     * @param customerMessageTypeId
     * @return
     */
    @PostMapping("delete")
    public ResponseResult deleteCustomerMessageType(@RequestParam("customerMessageTypeId")Integer customerMessageTypeId) {
        mCustomerMessageTypeService.deleteCustomerMessageType(customerMessageTypeId);
        return success();
    }


}
