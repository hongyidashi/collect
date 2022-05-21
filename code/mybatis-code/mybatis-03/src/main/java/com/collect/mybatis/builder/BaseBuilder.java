package com.collect.mybatis.builder;

import com.collect.mybatis.session.Configuration;

/**
 * @description: 构建器的基类，建造者模式
 * @author: panhongtong
 * @date: 2022/5/20 09:07
 **/
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
