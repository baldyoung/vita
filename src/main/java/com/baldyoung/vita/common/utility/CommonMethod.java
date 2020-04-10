package com.baldyoung.vita.common.utility;

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
}
