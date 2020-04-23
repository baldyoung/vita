package com.baldyoung.vita.common.utility;

import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;

import javax.servlet.http.HttpSession;
import java.util.Collection;

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
        //return 0;
    }
}
