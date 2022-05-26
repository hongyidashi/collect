package com.collect.mybatis.builder;

import com.collect.mybatis.session.Configuration;
import com.collect.mybatis.type.TypeAliasRegistry;

/**
 * @description: 构建器的基类，建造者模式
 * @author: panhongtong
 * @date: 2022/5/20 09:07
 **/
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
