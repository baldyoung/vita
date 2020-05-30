package com.baldyoung.vita.section;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.ExceptionBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.baldyoung.vita.common.pojo.dto.ResponseResult.error;

@ControllerAdvice
public class MerchantExceptionHandler {

    /**
     * 系统业务异常处理
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(ExceptionBase.class)
    @ResponseBody
    public ResponseResult doExceptionBase(ExceptionBase e, HttpServletResponse response) {
        //out.println(this.getClass()+"get message");
        return error(e.getCode(), e.getDesc());
    }

    /**
     * 系统其它异常处理
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult doException(Exception e, HttpServletResponse response) {
        //out.println(this.getClass()+"get message");
        return error();
    }


}
