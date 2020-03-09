package com.baldyoung.vita.common.pojo.exception;

public class EntityCheckException extends ExceptionBase {

    public EntityCheckException(String desc, Integer code) {
        super(desc, code);
    }

    public EntityCheckException(String desc) {
        super(desc);
    }

    public EntityCheckException() {
    }
}
