package org.collect.registry.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.collect.registry.common.api.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.collect.registry.common.api.ResultCode.INTERNAL_SERVER_ERROR;


/**
 * 描述：自定义异常控制器，用于拦截部分常见异常
 * 作者: panhongtong
 * 创建时间: 2020-05-13 16:01
 **/
@ControllerAdvice
@Slf4j
public class CustomizeExceptionController {

    /**
     * 拦截捕捉自定义异常 MessageCenterException.class
     */
    @ResponseBody
    @ExceptionHandler(value = MessageCenterException.class)
    public ResponseVo messageCenterExceptionHandler(MessageCenterException ex) {
        if (ex.getException() == null) {
            // 若只是单纯的像非空这样的条件判断自定义的异常，则显示ex即可
            log.error(ex.getMessage(), ex);
        } else {
            // 否则若是发生错误，那么需要将具体的异常信息写入
            log.error(ex.getMessage(), ex.getException());
        }
        return ResponseVo.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 拦截所有未被捕获的错误，并默认送出500异常状态码
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public ResponseVo exceptionHandler(Throwable ex) {
        log.error("未知异常，请联系开发人员", ex);
        return ResponseVo.fail(INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }

}
