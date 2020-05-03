package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.customer.service.CProductAttributeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;

@RestController
@RequestMapping("attribute")
public class CProductAttributeController {

    @Autowired
    private CProductAttributeServiceImpl cProductAttributeService;

    @GetMapping("all")
    public ResponseResult getAllAttributeData() {
        return success(cProductAttributeService.getAllAttributeData());
    }


}
