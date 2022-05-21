package com.collect.mybatis.session;

import com.collect.mybatis.builder.xml.XMLConfigBuilder;
import com.collect.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @description: SqlSessionFactoryBuilder 整个mybatis的入口类
 * @author: panhongtong
 * @date: 2022/5/19 20:41
 **/
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}
