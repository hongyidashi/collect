package com.collect.mybatis.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description: mapper代理
 * @author: panhongtong
 * @date: 2022/5/17 22:35
 **/
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口名称+方法名称 <--> 数据库语句操作
     */
    private final Map<String, String> sqlSession;

    /**
     * 被代理的接口
     */
    private final Class<T> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是 Object 提供的 toString、hashCode 等方法是不需要代理执行的
        // 所以添加 Object.class.equals(method.getDeclaringClass()) 判断
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            return "被代理了 " + sqlSession.get(mapperInterface.getName() + "." + method.getName());
        }
    }
}
