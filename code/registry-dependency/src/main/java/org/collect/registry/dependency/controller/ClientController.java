package org.collect.registry.dependency.controller;

import org.collect.registry.common.bean.Instance;
import org.collect.registry.dependency.manager.RegistryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 描述: 健康检查控制器
 *
 * @author: panhongtong
 * @date: 2021-05-02 21:31
 **/
@RestController
@RequestMapping("/registry")
public class ClientController {

    @Autowired
    private RegistryManager registryManager;

    /**
     * 健康检查接口
     */
    @GetMapping("/healthy")
    public String healthy() {
        return "healthy";
    }

    /**
     * 接收服务信息接口
     *
     * @param serverInfos 服务信息
     */
    @PostMapping("receive")
    public String receive(@RequestBody Map<String, Set<Instance>> serverInfos) {
        registryManager.setRegistryInfo(serverInfos);
        return "ok";
    }
}
