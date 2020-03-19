package com.baldyoung.vita.merchant.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.merchant.service.ProductAttributeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;
@RestController
@RequestMapping("mProductAttribute")
public class ProductAttributeController {

    @Autowired
    private ProductAttributeServiceImpl productAttributeService;

    @GetMapping("attributeTypeList")
    public ResponseResult getAllAttributeType() {
        return success(productAttributeService.getAllAttributeType());
    }

    @GetMapping("attributeValueList")
    public ResponseResult getAllAttributeValue() {
        return success(productAttributeService.getAllAttributeValue());
    }
}
