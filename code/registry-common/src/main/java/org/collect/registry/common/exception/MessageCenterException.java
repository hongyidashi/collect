package org.collect.registry.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.collect.registry.common.api.IResultCode;

/**
 * 描述：自定义异常类
 * 作者：panhongtong
 * CustomizeExceptionController
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageCenterException extends RuntimeException {
    private String message;
    private Integer code;
    private Throwable exception;

    /**
     * @param errorEnum 错误码枚举类
     * @param exception 错误信息
     */
    public MessageCenterException(IResultCode errorEnum, Throwable exception) {
        super(errorEnum.getMessage(), exception);//将错误压入错误栈中
        this.message = errorEnum.getMessage();
        this.exception = exception;
        this.code = errorEnum.getCode();
    }

    /**
     * @param errorEnum 错误码枚举类
     */
    public MessageCenterException(IResultCode errorEnum) {
        this.message = errorEnum.getMessage();
        this.exception = null;
        this.code = errorEnum.getCode();
    }

    /**
     * @param errorEnum 错误码枚举类
     * @param exception 错误信息
     * @param message   自定义错误说明
     */
    public MessageCenterException(IResultCode errorEnum, Throwable exception, String message) {
        super(errorEnum.getMessage(), exception);//将错误压入错误栈中
        this.message = message;
        this.exception = exception;
        this.code = errorEnum.getCode();
    }

    /**
     * @param errorEnum 错误码枚举类
     * @param message   自定义错误说明
     */
    public MessageCenterException(IResultCode errorEnum, String message) {
        this.message = message;
        this.exception = null;
        this.code = errorEnum.getCode();
    }

}