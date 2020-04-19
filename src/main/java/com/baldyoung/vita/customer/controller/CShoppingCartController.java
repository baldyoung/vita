package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.customer.service.CShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.getRoomIdFromSession;

/**
 * 购物车相关
 */
@RestController
@RequestMapping("shoppingCart")
public class CShoppingCartController {

    @Autowired
    private CShoppingCartServiceImpl cShoppingCartService;

    @GetMapping("addProduct")
    public ResponseResult addProduct(@RequestParam("productId")Integer productId,
                                     @RequestParam("quantity")Integer quantity,
                                     HttpSession session) {
        Integer roomId = getRoomIdFromSession(session);
        cShoppingCartService.addProductIntoShoppingCart(roomId, productId, quantity);
        return success();
    }



}
