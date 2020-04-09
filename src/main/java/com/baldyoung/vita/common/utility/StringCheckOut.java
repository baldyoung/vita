package com.baldyoung.vita.common.utility;

import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;

public class StringCheckOut {

    public static Integer outForInteger(String string) throws UtilityException {
        if (null != string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                throw new UtilityException("非整数格式");
            }
        }
        throw new UtilityException("字符串为null");
    }

    public static Integer outForIntegerWithRange(String string, Integer min, Integer max) throws UtilityException {
        if (null == min || null == max) {
            throw new UtilityException("最小值或者最大值不能为null");
        }
        Integer integer = outForInteger(string);
        if (integer.compareTo(min) < 0) {
            throw new UtilityException("数值小于" + min);
        }
        if (integer.compareTo(max) > 0) {
            throw new UtilityException("数值大于" + max);
        }
        return integer;
    }


}
