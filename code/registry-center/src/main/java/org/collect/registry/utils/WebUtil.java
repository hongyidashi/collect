package org.collect.registry.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 描述: Web工具类
 *
 * @author: panhongtong
 * @date: 2021-05-02 14:35
 **/
public class WebUtil {

    /**
     * 获取指定参数，若未获取到则抛出异常
     *
     * @param request 请求对象
     * @param key     参数名
     * @return 参数值
     */
    public static String required(final HttpServletRequest request, final String key) {
        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("参数 " + key + " 是必须的，不能为空");
        }
        String encoding = request.getParameter("encoding");
        return resolveValue(value, encoding);
    }

    /**
     * 获取指定参数，若未获取到则使用默认值
     *
     * @param request      请求对象
     * @param key          参数名
     * @param defaultValue 默认值
     * @return 参数值
     */
    public static String optional(final HttpServletRequest request, final String key, final String defaultValue) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey(key) || parameterMap.get(key)[0] == null) {
            return defaultValue;
        }

        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }

        return resolveValue(value, request.getParameter("encoding"));
    }

    /**
     * 解码目标值
     *
     * @param value    目标值
     * @param encoding 编码方式
     * @return 解码后的值
     */
    private static String resolveValue(String value, String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            encoding = StandardCharsets.UTF_8.name();
        }
        try {
            value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
        } catch (UnsupportedEncodingException ignore) {
        }
        return value.trim();
    }
}
