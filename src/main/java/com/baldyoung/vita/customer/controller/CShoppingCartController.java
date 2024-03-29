package com.baldyoung.vita.customer.controller;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.dto.shoppingCart.SubmitData;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.service.CShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.success;
import static com.baldyoung.vita.common.utility.CommonMethod.getHeartBeatKeyFromSession;
import static com.baldyoung.vita.common.utility.CommonMethod.getRoomIdFromSession;

/**
 * 购物车相关
 */
@RestController
@RequestMapping("shoppingCart")
public class CShoppingCartController {

    public static final String SHOPPING_CART_HEART_BEAT_KEY = "hb-key";

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
                                     HttpSession session) throws UtilityException, ServiceException {
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
    public ResponseResult getAllProductItemFromShoppingCart(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        return success(cShoppingCartService.getALLItemFromShoppingCart(roomId));
    }

    /**
     * 删除指定商品
     * @param productIdList
     * @param session
     * @return
     * @throws UtilityException
     */
    @PostMapping("itemDelete")
    public ResponseResult deleteProduct(@RequestParam("productIdList[]") List<Integer> productIdList,
                                        HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        cShoppingCartService.deleteProduct(roomId, productIdList.toArray(new Integer[productIdList.size()]));
        return success();
    }

    /**
     * 变更购物车的状态为“提交中”
     * 不允许购物车数据再被修改
     * @param session
     * @return
     * @throws UtilityException
     * @throws ServiceException
     */
    @PostMapping("readySubmit")
    public ResponseResult readyToSubmitShoppingCart(@RequestBody SubmitData submitData,
                                                    HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        String key = cShoppingCartService.readySubmitShoppingCart(roomId, submitData.getDiningTime(), submitData.getDiningType(), submitData.getItemList());
        session.setAttribute(SHOPPING_CART_HEART_BEAT_KEY, key);
        return success();
    }

    /**
     * 心跳交互
     * 心跳期间代表客户端还在对购物车进行下单操作
     * @param session
     * @return
     * @throws UtilityException
     * @throws ServiceException
     */
    @GetMapping("heartBeat")
    public ResponseResult doHeartBeat(HttpSession session) throws UtilityException, ServiceException {
        Integer roomId = getRoomIdFromSession(session);
        String key = getHeartBeatKeyFromSession(session);
        cShoppingCartService.doHeartBeat(roomId, key);
        return success();
    }

    /**
     * 变更购物车状态为“可修改”
     * 可以修改购物车数据
     * @param session
     * @return
     * @throws UtilityException
     */
    @GetMapping("cancelReady")
    public ResponseResult cancelReadyForSubmitShoppingCart(HttpSession session) throws UtilityException {
        Integer roomId = getRoomIdFromSession(session);
        cShoppingCartService.cancelSubmitShoppingCart(roomId);
        return success();
    }



}
