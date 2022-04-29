package com.collect.nginx;

import nginx.clojure.java.StringFacedJavaBodyFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 请求响应过滤器 string
 * @author: panhongtong
 * @date: 2022/4/28 17:11
 **/
public class StringFacedUppercaseBodyFilter extends StringFacedJavaBodyFilter {
    @Override
    protected Object[] doFilter(Map<String, Object> request, String body, boolean isLast) throws IOException {
        if (isLast) {
            // isLast等于true，表示当前web请求过程中最后一次调用doFilter方法，
            // body是完整response body的最后一部分，
            // 此时返回的status应该不为空，这样nginx-clojure框架就会完成body filter的执行流程，将status和聚合后的body返回给客户端
            return new Object[]{200, null, body.toUpperCase()};
        } else {
            // isLast等于false，表示当前web请求过程中，doFilter方法还会被继续调用，当前调用只是多次中的一次而已，
            // body是完整response body的其中一部分，
            // 此时返回的status应该为空，这样nginx-clojure框架就继续body filter的执行流程，继续调用doFilter
            return new Object[]{null, null, body.toUpperCase()};
        }
    }
}
