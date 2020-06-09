package com.baldyoung.vita.section;

import com.baldyoung.vita.common.pojo.dto.ResponseResult;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
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
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseResult doServiceException(ServiceException e, HttpServletResponse response) {
        return error(e.getCode(), e.getDesc());
    }

    /**
     * 系统工具类处理
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(UtilityException.class)
    @ResponseBody
    public ResponseResult doUtilityException(UtilityException e, HttpServletResponse response) {
        // 顾客端，身份失效
        if (200100100 == e.getCode().intValue()) {
            return error(null, null);
        }
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
