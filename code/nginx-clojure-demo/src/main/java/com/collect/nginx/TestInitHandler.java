package com.collect.nginx;

import nginx.clojure.NginxClojureRT;
import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 初始化处理器
 * @author: panhongtong
 * @date: 2022/4/27 17:20
 **/
public class TestInitHandler implements NginxJavaRingHandler {
    @Override
    public Object[] invoke(Map<String, Object> map) throws IOException {
        // 可以根据实际需求执行初始化操作，这里作为演示，只打印日志
        NginxClojureRT.log.info("TestInitHandler.invoke executed");
        return new Object[0];
    }
}
