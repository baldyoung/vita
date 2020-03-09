package com.baldyoung.vita.common.utility;

public class CommonMethod {

    public static String ObjectToString(Object  object) {
        if (null == object) {
            return null;
        }
        return object.toString();
    }
}
