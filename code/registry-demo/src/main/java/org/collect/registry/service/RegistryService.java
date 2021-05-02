package org.collect.registry.service;

import javax.servlet.http.HttpServletRequest;

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
}
