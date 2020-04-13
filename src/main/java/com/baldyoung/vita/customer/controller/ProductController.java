package com.baldyoung.vita.customer.controller;


import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.*;

@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping("validProductForType")
    public ResponseResult getValidProductList(@RequestParam("typeId")Integer typeId) {

        return success();
    }


}
