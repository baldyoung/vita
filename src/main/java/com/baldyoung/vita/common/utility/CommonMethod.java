package com.baldyoung.vita.common.utility;

import com.baldyoung.vita.common.pojo.dto.orderItem.COrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.customer.controller.CShoppingCartController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Collection;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.MERCHANT_GRADE_FORBIDDEN;
import static com.baldyoung.vita.common.pojo.enums.systemEnums.SystemExceptionEnums.ROOM_ID_NOT_EXISTS;

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
        Object object = session.getAttribute("roomId");
        if (null == object) {
            throw new UtilityException(ROOM_ID_NOT_EXISTS);
        }
        return Integer.parseInt(String.valueOf(object));
    }

    public static Integer getMerchantUserIdFromSession(HttpSession session) throws UtilityException {
        Object object = session.getAttribute("merchantUserId");
        if (null == object) {
            throw new UtilityException(MERCHANT_GRADE_FORBIDDEN);
        }
        return Integer.parseInt(String.valueOf(object));
    }

    public static Integer getMerchantUserGradeFromSession(HttpSession session) throws UtilityException {
        Object object = session.getAttribute("grade");
        if (null == object) {
            throw new UtilityException(MERCHANT_GRADE_FORBIDDEN);
        }
        return Integer.parseInt(String.valueOf(object));
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


    public static void readFileAndSendData(OutputStream outputStream, String filePathName) {
        File file = new File(filePathName);
        if (!file.exists() || file.isDirectory()) {
            return ;
        }
        // 创建文件读取流
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return ;
        }
        byte[] bytes = new byte[2048];
        int k;
        String logHead = "<html><head></head><body><textarea style='width:100%; height:100%; resize:none; background:black; color:#2BF100; padding: 10px; font-size:18px;'>";
        String logEnd = "</textarea></body></html>";
        try {
            outputStream.write(logHead.getBytes());
            while(true) {
                k = fileInputStream.read(bytes);
                if (k == -1) {
                    break;
                }
                outputStream.write(bytes, 0, k);
            }
            outputStream.write(logEnd.getBytes());
        } catch (IOException e) {
            // System.out.println("readFileAndSendData error: filePathName = "+filePathName + "\n" + e.getMessage());
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // System.out.println("readFileAndSendData error: filePathName = "+filePathName + "\n" + e.getMessage());
                }
            }
        }

    }
}
