package com.baldyoung.vita.common.pojo.exception;

public class UtilityException extends ExceptionBase {

    public UtilityException (String desc, Integer code) {
        super(desc, code);
    }

    public UtilityException (String desc) {
        super(desc);
    }

    public UtilityException () {
        super();
    }

}
