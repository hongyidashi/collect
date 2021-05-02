package org.collect.dependency.registry;

import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

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

    @Value("${spring.application.name}")
    private String serverName;

    @Value("${app.registry.addr}")
    private String registryAddr;

    @Value("${app.registry.enable}")
    private String enable;

    /**
     * 执行注册逻辑
     *
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args){
        String port = environment.getProperty("local.server.port");

        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException ignore) {
        }
        assert localHost != null;
        assert port != null;
        String ip = localHost.getHostAddress();

        Map<String, Object> param = Maps.newHashMap();
        param.put("port", Integer.parseInt(port));
        param.put("ip", ip);
        param.put("server_name", serverName);
        param.put("enable", Boolean.parseBoolean(enable));
        String addr = registryAddr + "/registry";
        String result = HttpUtil.get(addr, param);
        log.info("registry result:{}", result);
    }

}
