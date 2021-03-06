package org.collect.registry.center.service;

import org.collect.registry.common.bean.Instance;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-05-02 14:19
 **/
public interface RegistryService {

    /**
     * 服务注册
     *
     * @param request 请求对象
     * @return 是否注册成功
     */
    String registry(HttpServletRequest request);

    /**
     * 获取服务信息，若服务名为空，则获取所有
     *
     * @param serverName 服务名
     * @return 服务信息
     */
    Map<String, Set<Instance>> serverInfo(String serverName);
}
