package com.baldyoung.vita.merchant.controller;


import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.merchant.service.ProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;

@RestController
@RequestMapping("mProductType")
public class ProductTypeController {

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @GetMapping("all")
    public ResponseResult getAllType() {
        return success(productTypeService.getAllProductType());
    }
}
