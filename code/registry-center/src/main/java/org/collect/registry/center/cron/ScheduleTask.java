package org.collect.registry.center.cron;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.collect.registry.center.manager.ServerManager;
import org.collect.registry.common.bean.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述: 定时任务
 *
 * @author: panhongtong
 * @date: 2021-05-03 17:34
 **/
@Slf4j
@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private ServerManager serverManager;

    /**
     * 定时健康检查
     */
    @Scheduled(fixedRate = 5000)
    public void ckeckServer() {
        Map<String, Set<Instance>> serverInfo = serverManager.getServerInfo(null);
        if (CollUtil.isEmpty(serverInfo)) {
            log.info("\n服务列表为空，不用心跳检查~~~QAQ");
            return;
        }

        int healthyCount = 0;
        int badCount = 0;
        int removeCount = 0;

        List<Instance> instances = serverInfo.values().stream()
                .flatMap(Collection::stream).collect(Collectors.toList());

        for (Instance instance : instances) {
            Boolean healthy = serverManager.checkHealthy(instance);
            // 移除多次通信失败(不健康)的实例
            if (!healthy) {
                badCount++;
                instance.setLossCount(instance.getLossCount() + 1);
                instance.setStatus(Instance.UNHEALTHY);
                log.info("\n发现不健康实例：{}", instance);

                if (instance.getLossCount() >= ServerManager.MAX_LOSS_COUNT) {
                    serverManager.removeInstance(instance);
                    removeCount++;
                    continue;
                }
            }
            healthyCount++;
        }
        log.info("\n当前健康实例数：{}，不健康实例数：{}，移除实例数：{}", healthyCount, badCount, removeCount);
    }
}
