package com.collect.nginx;

import nginx.clojure.java.Constants;
import nginx.clojure.java.NginxJavaHeaderFilter;

import java.util.Map;

/**
 * @description: 请求头过滤器
 * @author: panhongtong
 * @date: 2022/4/28 17:02
 **/
public class RemoveAndAddMoreHeaders implements NginxJavaHeaderFilter {
    @Override
    public Object[] doFilter(int status, Map<String, Object> request, Map<String, Object> responseHeaders) {
        // 先删再加，相当于修改了Content-Type的值
        responseHeaders.remove("Content-Type");
        responseHeaders.put("Content-Type", "text/html");

        // 增加两个header
        responseHeaders.put("Auth-Header", "token");
        responseHeaders.put("Server", "Test-Server");

        // 返回PHASE_DONE表示告知nginx-clojure框架，当前filter正常，可以继续执行其他的filter和handler
        return Constants.PHASE_DONE;
    }
}
