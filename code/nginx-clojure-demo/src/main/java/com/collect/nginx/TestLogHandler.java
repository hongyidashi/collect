package com.collect.nginx;

import nginx.clojure.Configurable;
import nginx.clojure.NginxClojureRT;
import nginx.clojure.java.NginxJavaRequest;
import nginx.clojure.java.NginxJavaRingHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @description: 日志处理器
 * @author: panhongtong
 * @date: 2022/4/28 14:40
 **/
public class TestLogHandler implements NginxJavaRingHandler, Configurable {

    /**
     * 是否将User Agent打印在日志中
     */
    private boolean logUserAgent;

    /**
     * 日志文件路径
     */
    private String filePath;

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        File file = new File(filePath);
        NginxJavaRequest r = (NginxJavaRequest) request;
        try (FileOutputStream out = new FileOutputStream(file, true)) {
            String msg = String.format("%s - %s [%s] \"%s\" %s \"%s\" %s %s\n",
                    r.getVariable("remote_addr"),
                    r.getVariable("remote_user", "x"),
                    r.getVariable("time_local"),
                    r.getVariable("request"),
                    r.getVariable("status"),
                    r.getVariable("body_bytes_sent"),
                    r.getVariable("http_referer", "x"),
                    logUserAgent ? r.getVariable("http_user_agent") : "-");
            out.write(msg.getBytes("utf8"));
        }
        return new Object[0];
    }

    @Override
    public void config(Map<String, String> properties) {
        logUserAgent = "on".equalsIgnoreCase(properties.get("log.user.agent"));
        filePath = properties.get("log.file.path");
        NginxClojureRT.log.info("TestLogHandler, logUserAgent [" + logUserAgent + "], filePath [" + filePath + "]");
    }
}
