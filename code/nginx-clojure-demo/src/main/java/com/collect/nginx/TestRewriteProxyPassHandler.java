package com.collect.nginx;

import nginx.clojure.NginxClojureRT;
import nginx.clojure.java.NginxJavaRequest;
import nginx.clojure.java.NginxJavaRingHandler;

import java.util.Map;

import static nginx.clojure.java.Constants.PHASE_DONE;

/**
 * @description: 重定向处理器
 * @author: panhongtong
 * @date: 2022/4/27 17:23
 **/
public class TestRewriteProxyPassHandler implements NginxJavaRingHandler {

    @Override
    public Object[] invoke(Map<String, Object> req) {
        // 根据业务情况定制计算出的path
        String testHost = computeHost(req);
        // 用setVariable方法设置test_host变量的值，这个test_host在这个location中被定义，跳转的时候就用这个值作为path
        ((NginxJavaRequest) req).setVariable("test_host", testHost);
        // 返回PHASE_DONE之后，nginx-clojure框架就会执行proxy_pass逻辑，
        // 如果返回的不是PHONE_DONE，nginx-clojure框架就把这个handler当做content handler处理
        return PHASE_DONE;
    }

    /**
     * 这里写入业务逻辑，根据实际情况确定返回的path
     *
     * @param req req
     * @return host
     */
    private String computeHost(Map<String, Object> req) {
        // 确认是http还是https
        String scheme = (String) req.get("scheme");
        // 确认端口号
        String serverPort = (String) req.get("server-port");

        // /contentdemo是nginx.conf中配置的一个location，您可以根据自己的业务情况来决定返回值
        String testHost = scheme + "://127.0.0.1:" + serverPort + "/contentdemo";
        NginxClojureRT.log.info("pass address [" + testHost + "]");

        return testHost;
    }
}