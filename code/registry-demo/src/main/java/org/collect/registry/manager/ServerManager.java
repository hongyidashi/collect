package org.collect.registry.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.collect.registry.bean.Instance;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 描述: 服务管理器
 *
 * @author: panhongtong
 * @date: 2021-05-02 14:17
 **/
@Component
public class ServerManager {

    /**
     * http请求相关字符
     */
    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTP_PORT_PRE = ":";

    /**
     * 健康检查接口路径
     */
    private static final String HEALTHY_PATH = "/registry/status";

    /**
     * 服务名：服务实例集合
     */
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Instance>> SERVERS = MapUtil.newConcurrentHashMap();

    /**
     * 注册一个服务实例
     *
     * @param instance 服务实例
     * @return 是否注册成功
     */
    public Boolean registryInstance(Instance instance) {
        CopyOnWriteArrayList<Instance> instances = SERVERS
                .computeIfAbsent(instance.getServerName(), name -> new CopyOnWriteArrayList<>());
        instances.add(instance);
        return Boolean.TRUE;
    }

    /**
     * 检查一个服务实例是否健康
     *
     * @param instance 实例对象
     * @return 是否健康
     */
    public Boolean checkHealthy(final Instance instance) {
        HttpResponse response = HttpUtil.createGet(buildHealthyReqUrl(instance)).execute();
        // 正常响应则认为是健康状态
        return response.isOk();
    }

    /**
     * 构建健康检查接口url
     *
     * @param instance 服务实例
     * @return URL
     */
    private String buildHealthyReqUrl(Instance instance) {
        return HTTP_PROTOCOL + instance.getIp() +
                HTTP_PORT_PRE + instance.getPort() +
                HEALTHY_PATH;
    }
}
