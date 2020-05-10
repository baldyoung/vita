package com.baldyoung.vita.merchant.section;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.error;

@ControllerAdvice
public class MerchantExceptionHandler {

    @ExceptionHandler(ExceptionBase.class)
    @ResponseBody
    public ResponseResult doThat(ServiceException e, HttpServletResponse response) {
        //out.println(this.getClass()+"get message");
        return error(e.getCode(), e.getDesc());
    }

}
