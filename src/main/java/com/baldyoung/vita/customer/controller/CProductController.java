package com.baldyoung.vita.customer.controller;


import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.customer.service.CProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.getRoomIdFromSession;

@RestController
@RequestMapping("product")
public class CProductController {

    @Autowired
    private CProductServiceImpl cProductService;

    /**
     * 获取指定品类的所有上架商品
     * @param typeId
     * @return
     */
    @GetMapping("validProductForType")
    public ResponseResult getValidProductList(@RequestParam("t")Integer typeId, HttpServletRequest request) {
        Integer roomId = getRoomIdFromSession(request.getSession());
        return success(cProductService.getValidProductForProductType(typeId, roomId));
    }




}
