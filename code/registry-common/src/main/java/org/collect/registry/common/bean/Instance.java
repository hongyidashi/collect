package org.collect.registry.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 描述: 服务实例
 *
 * @author: panhongtong
 * @date: 2021-05-02 15:21
 **/
@Data
public class Instance implements Serializable {

    /**
     * 服务状态值
     */
    public static int DOWN = 0;
    public static int UP = 1;

    /**
     * 字段默认字符串值
     */
    public static final String ENABLE = "false";
    public static final String PORT = "8080";
    public static final String IP = "127.0.0.1";
    public static final String HEALTHY = "false";
    public static final String STATUS = "1";

    /**
     * 字段名
     */
    public static final String PARAM_ENABLE = "enable";
    public static final String PARAM_PORT = "port";
    public static final String PARAM_IP = "ip";
    public static final String PARAM_SERVERNAME = "server_name";
    public static final String PARAM_HEALTHY = "healthy";
    public static final String PARAM_STATUS = "status";

    /**
     * ip
     */
    private String ip;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 服务是否健康
     */
    private Boolean healthy;

    /**
     * 服务状态
     */
    private Integer status;

    /**
     * 是否开启注册功能
     */
    private Boolean enable;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Instance instance = (Instance) o;
        return Objects.equals(ip, instance.ip) && Objects.equals(port, instance.port) && Objects.equals(serverName, instance.serverName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, serverName);
    }
}
