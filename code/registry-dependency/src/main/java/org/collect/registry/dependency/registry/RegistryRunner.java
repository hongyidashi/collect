package org.collect.registry.dependency.registry;

import lombok.extern.slf4j.Slf4j;
import org.collect.registry.common.bean.Instance;
import org.collect.registry.dependency.manager.RegistryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 描述: 触发注册的钩子
 *
 * @author: panhongtong
 * @date: 2021-05-02 21:38
 **/
@Slf4j
public class RegistryRunner implements ApplicationRunner {

    @Autowired
    private Environment environment;
    @Autowired
    private RegistryManager registryManager;

    @Value("${spring.application.name}")
    private String serverName;
    @Value("${app.registry.enable}")
    private String enable;

    /**
     * 执行注册逻辑
     * <p>
     * 注册成功后拉取服务信息
     *
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args) {
        Instance instance = generateInstance();
        registryManager.registry(instance);
        registryManager.pullServerInfo();
    }

    /**
     * 生成服务实例对象
     *
     * @return 服务实例对象
     */
    private Instance generateInstance() {
        String port = environment.getProperty("local.server.port");
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException ignore) {
        }
        String ip = localHost.getHostAddress();
        Instance instance = new Instance();
        instance.setEnable(Boolean.parseBoolean(enable));
        instance.setPort(Integer.parseInt(port));
        instance.setIp(ip);
        instance.setServerName(serverName);
        return instance;
    }


}
