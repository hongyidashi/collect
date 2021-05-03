package org.collect.registry.center.contoller;

import org.collect.registry.center.service.RegistryService;
import org.collect.registry.common.bean.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

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
    @GetMapping("registry")
    public String registry(HttpServletRequest request) {
        return registryService.registry(request);
    }

    /**
     * 获取服务信息，若服务名为空，则获取所有
     *
     * @param serverName 服务名
     * @return 服务信息
     */
    @GetMapping("serverInfo")
    public Map<String, Set<Instance>> serverInfo(@RequestParam(value = "serverName",required = false) String serverName) {
        return registryService.serverInfo(serverName);
    }
}
