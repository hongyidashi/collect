package org.collect.registry.contoller;

import org.collect.registry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: 注册接口
 *
 * @author: panhongtong
 * @date: 2021-05-02 11:20
 **/
@RestController
public class RegistryController {

    @Autowired
    private RegistryService registryService;

    /**
     * 服务注册
     *
     * @param request 请求对象
     * @return 是否注册成功
     */
    @PostMapping("registry")
    public String registry(HttpServletRequest request) {
        return registryService.registry(request);
    }

}
