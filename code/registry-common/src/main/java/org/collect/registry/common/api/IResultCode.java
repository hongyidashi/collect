package org.collect.registry.common.api;

import java.io.Serializable;

/**
 * 描述: 响应码
 * 作者: panhongtong
 * 创建时间: 2021-01-21 15:22
 **/
public interface IResultCode extends Serializable {

    /**
     * 消息
     *
     * @return String
     */
    String getMessage();

    /**
     * 状态码
     *
     * @return int
     */
    int getCode();

}
