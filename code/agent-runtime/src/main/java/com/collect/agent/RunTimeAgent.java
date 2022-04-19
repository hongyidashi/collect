package com.collect.agent;

import com.collect.agent.transformer.RunTimeTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @description: 代理入口
 * @author: panhongtong
 * @date: 2022/4/19 10:50
 **/
public class RunTimeAgent {

    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("探针启动！！！");
        System.out.println("探针传入参数：" + arg);
        instrumentation.addTransformer(new RunTimeTransformer());
    }

}
