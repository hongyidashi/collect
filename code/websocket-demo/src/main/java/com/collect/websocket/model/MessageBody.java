package com.collect.websocket.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述: 传输消息的实体类
 * 作者: panhongtong
 * 创建时间: 2020-12-27 22:56
 **/
@Data
@Accessors(chain = true)
public class MessageBody {
    /**
     * 消息内容
     */
    private String content;

    /**
     * 广播转发的目标地址（告知 STOMP 代理转发到哪个地方）
     */
    private String destination;
}
