package com.collect.quartz.config;

import com.collect.quartz.job.QuartzJob1;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 描述: Quartz配置
 * 作者: panhongtong
 * 创建时间: 2020-12-29 21:56
 **/
@Configuration
public class QuartzConfig {

    /**
     * 定义一个 JobDetail，添加任务实例
     * @return
     */
    @Bean
    public JobDetail jobDetail1(){
        return JobBuilder.newJob(QuartzJob1.class).storeDurably().build();
    }

    /**
     * 制定定时策略
     * @return
     */
    @Bean
    public Trigger trigger1(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                //每10秒执行一次
                .withIntervalInSeconds(10)
                //永久重复，一直执行下去
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail1())
                .withSchedule(scheduleBuilder)
                .build();
    }

    /**
     * 也可以基于内部类的方式定义
     * @return
     */
    @Bean
    public JobDetail jobDetail2(){
        QuartzJobBean quartzJob2 = new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                System.out.println("内部类quartzJob2----" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        };
        return JobBuilder.newJob(quartzJob2.getClass()).storeDurably().build();
    }

    @Bean
    public Trigger trigger2(){
        //JobDetail的bean注入不能省略
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                //每20秒执行一次
                .withIntervalInSeconds(20)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail2())
                .withSchedule(scheduleBuilder).build();
    }

}
