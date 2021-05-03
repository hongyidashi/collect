package org.collect.registry.center.service.impl;

import org.collect.registry.center.manager.ServerManager;
import org.collect.registry.center.service.RegistryService;
import org.collect.registry.center.utils.WebUtil;
import org.collect.registry.common.bean.Instance;
import org.collect.registry.common.constant.RespConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * 描述: 注册业务实现类
 *
 * @author: panhongtong
 * @date: 2021-05-02 14:22
 **/
@Service
public class RegistryServiceImpl implements RegistryService {

    @Autowired
    private ServerManager serverManager;

    /**
     * 服务注册
     *
     * @param request 请求对象
     * @return 是否注册成功
     */
    @Override
    public String registry(HttpServletRequest request) {
        // TODO 接住异常，响应回服务端
        final Instance instance = parseInstance(request);

        if (!instance.getEnable()) {
            return RespConstant.UNENABLE;
        }

        // 检测服务心跳
        instance.setHealthy(serverManager.checkHealthy(instance));

        // 注册服务
        if (!serverManager.registryInstance(instance)) {
            return RespConstant.FAIL;
        }

        // TODO 通知消费者更新注册信息


        return RespConstant.SUCCESS;
    }

    /**
     * 获取服务信息，若服务名为空，则获取所有
     *
     * @param serverName 服务名
     * @return 服务信息
     */
    @Override
    public Map<String, Set<Instance>> serverInfo(String serverName) {
        return serverManager.getServerInfo(serverName);
    }

    /**
     * 解析服务实例信息
     *
     * @param request 请求对象
     * @return 服务实例
     */
    private Instance parseInstance(HttpServletRequest request) {
        String serverNameStr = WebUtil.required(request, Instance.PARAM_SERVERNAME);
        String enableStr = WebUtil.optional(request, Instance.PARAM_ENABLE, Instance.ENABLE);
        String ipStr = WebUtil.optional(request, Instance.PARAM_IP, Instance.IP);
        String portStr = WebUtil.optional(request, Instance.PARAM_PORT, Instance.PORT);
        String healthyStr = WebUtil.optional(request, Instance.PARAM_HEALTHY,Instance.HEALTHY);
        String statusStr = WebUtil.optional(request, Instance.PARAM_STATUS,Instance.STATUS);

        Instance instance = new Instance();
        instance.setServerName(serverNameStr);
        instance.setEnable(Boolean.parseBoolean(enableStr));
        instance.setIp(ipStr);
        instance.setPort(Integer.parseInt(portStr));
        instance.setHealthy(Boolean.parseBoolean(healthyStr));
        instance.setStatus(Integer.parseInt(statusStr));
        instance.setLossCount(0);
        return instance;
    }

}
