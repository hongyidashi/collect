package com.collect.nginx;

import nginx.clojure.Configurable;
import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static nginx.clojure.MiniConstants.CONTENT_TYPE;
import static nginx.clojure.MiniConstants.NGX_HTTP_OK;

/**
 * @description: 内容处理器
 * @author: panhongtong
 * @date: 2022/4/27 17:21
 **/
public class TestContentHandler implements NginxJavaRingHandler, Configurable {

    private Map<String, String> config;

    /**
     * location中配置的content_handler_property属性会通过此方法传给当前类
     *
     * @param map map
     */
    @Override
    public void config(Map<String, String> map) {
        this.config = map;
    }

    @Override
    public Object[] invoke(Map<String, Object> map) throws IOException {
        String body = "From TestContentHandler, "
                + LocalDateTime.now()
                + ", foo : "
                + config.get("foo.name")
                + ", bar : "
                + config.get("bar.name");

        return new Object[]{
                // http status 200
                NGX_HTTP_OK,
                // headers map
                ArrayMap.create(CONTENT_TYPE, "text/plain"),
                body
        };
    }

}
