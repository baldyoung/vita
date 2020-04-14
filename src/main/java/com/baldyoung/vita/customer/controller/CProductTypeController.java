package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.customer.service.CProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("productType")
public class CProductTypeController {

    @Autowired
    private CProductTypeServiceImpl cProductTypeService;

    /**
     * 获取上架的品类集
     * @return
     */
    @GetMapping("list")
    public ResponseResult getValidProductTypeList() {
        return success(cProductTypeService.getAllValidProductType());
    }

}
