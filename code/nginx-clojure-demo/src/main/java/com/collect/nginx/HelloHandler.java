package com.collect.nginx;

import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static nginx.clojure.MiniConstants.CONTENT_TYPE;
import static nginx.clojure.MiniConstants.NGX_HTTP_OK;

/**
 * @description: 测试处理器
 * @author: panhongtong
 * @date: 2022/4/27 16:46
 **/
public class HelloHandler implements NginxJavaRingHandler {

    @Override
    public Object[] invoke(Map<String, Object> map) throws IOException {
        return new Object[]{
                // http status 200
                NGX_HTTP_OK,
                // headers map
                ArrayMap.create(CONTENT_TYPE, "text/plain"),
                // response body can be string, File or Array/Collection of them
                "Hello, Nginx clojure! " + LocalDateTime.now()
        };
    }

}
