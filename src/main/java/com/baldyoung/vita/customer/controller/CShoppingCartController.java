package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
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

    /**
     * 购物车新增商品
     * @param productId
     * @param quantity
     * @param session
     * @return
     * @throws UtilityException
     */
    @GetMapping("addProduct")
    public ResponseResult addProduct(@RequestParam("productId")Integer productId,
                                     @RequestParam("quantity")Integer quantity,
                                     HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        cShoppingCartService.addProductIntoShoppingCart(roomId, productId, quantity);
        return success();
    }

    /**
     * 获取当前购物车下的所有商品
     * @param session
     * @return
     * @throws UtilityException
     */
    @GetMapping("itemList")
    public ResponseResult getAllProductItemFromShoppingCart(HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cShoppingCartService.getALLItemFromShoppingCart(roomId));
    }



}
