package org.collect.registry.dependency.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.collect.registry.common.api.ResponseVo;
import org.collect.registry.common.bean.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 描述: 注册信息管理器
 *
 * @author: panhongtong
 * @date: 2021-05-02 23:31
 **/
@Slf4j
@Component
public class RegistryManager {

    @Value("${app.registry.addr}")
    private String registryAddr;

    /**
     * 注册信息 服务名：实例集合
     */
    private Map<String, Set<Instance>> registryInfo = Maps.newConcurrentMap();

    /**
     * 拉取服务信息
     */
    public void pullServerInfo() {
        String addr = getPullServerAddr();
        String result = HttpUtil.get(addr);
        ResponseVo<Map<String, Set<Instance>>> vo = JSONUtil.toBean(result, ResponseVo.class);
        if (vo.isSuccess()) {
            registryInfo = vo.getData();
            return;
        }
        log.error("服务拉取失败：{}", vo.getMsg());
    }

    /**
     * 注册服务实例
     *
     * @param instance 服务实例
     * @return 注册响应结果
     */
    public ResponseVo registry(Instance instance) {
        Map<String, Object> param = BeanUtil.beanToMap(instance, true, true);
        String addr = getRegistryAddr();
        String result = HttpUtil.get(addr, param);
        ResponseVo vo = JSONUtil.toBean(result, ResponseVo.class);
        if (!vo.isSuccess()) {
            log.error("服务注册失败：{}", vo.getMsg());
        }
        return vo;
    }

    /**
     * 获取拉取服务信息请求路径
     *
     * @return 拉取服务信息请求路径
     */
    private String getPullServerAddr() {
        return registryAddr + "/serverInfo";
    }

    /**
     * 获取注册请求路径
     *
     * @return 注册请求路径
     */
    private String getRegistryAddr() {
        return registryAddr + "/registry";
    }

}
