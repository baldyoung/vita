package com.baldyoung.vita.common.utility;

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

    public static Integer getRoomIdFromSession(HttpSession session) {
        return 0;
        /*Object object = session.getAttribute("roomId");
        if (null == object) {
            return null;
        }
        return Integer.parseInt(String.valueOf(object));*/
    }
}
