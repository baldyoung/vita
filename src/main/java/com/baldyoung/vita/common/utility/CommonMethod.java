package com.baldyoung.vita.common.utility;

import com.baldyoung.vita.common.pojo.dto.orderItem.COrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.controller.CShoppingCartController;

import javax.servlet.http.HttpSession;
import java.util.Collection;

public class CommonMethod {

    public static String ObjectToString(Object  object) {
        if (null == object) {
            return null;
        }
        return object.toString();
    }

    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        }
        String str = object.toString();
        return str.trim().equals("");
    }

    public static boolean isAnyEmpty(Object... objects) {
        if (null == objects) {
            return true;
        }
        for (Object object : objects) {
            if (isEmpty(object)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmptyCollection(Collection collection) {
        if (null == collection) {
            return true;
        }
        return collection.size() == 0;
    }

    public static Integer toInteger(Object object) {
        if (null == object) {
            return null;
        }
        Integer result;
        try {
            result = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
            return null;
        }
        return result;
    }

    public static Integer getRoomIdFromSession(HttpSession session) throws UtilityException {
        return 333;
        /*Object object = session.getAttribute("roomId");
        if (null == object) {
            throw new UtilityException(ROOM_ID_NOT_EXISTS);
        }
        return Integer.parseInt(String.valueOf(object));
        */
    }

    public static String getHeartBeatKeyFromSession(HttpSession session) {
        Object object = session.getAttribute(CShoppingCartController.SHOPPING_CART_HEART_BEAT_KEY);
        return String.valueOf(object);
    }

    public static COrderItemDto toCOrderItemDto(OrderItemEntity entity) {
        COrderItemDto dto = new COrderItemDto();
        dto.setOrderId(entity.getOrderId());
        dto.setOrderProductItemId(entity.getOrderProductItemId());
        dto.setOrderProductId(entity.getOrderProductId());
        dto.setOrderProductImg(entity.getOrderProductImg());
        dto.setOrderProductItemStatusFlag(entity.getOrderProductItemStatusFlag());
        dto.setOrderProductItemStatusDesc(entity.getOrderProductItemStatusDesc());
        dto.setOrderProductName(entity.getOrderProductName());
        dto.setOrderProductPrice(entity.getOrderProductPrice());
        dto.setOrderProductQuantity(entity.getOrderProductQuantity());
        dto.setOrderProductRemarks(entity.getOrderProductRemarks());
        return dto;
    }
}
