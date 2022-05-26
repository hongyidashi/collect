package com.collect.mybatis.datasource.pooled;

import com.collect.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @description: 有连接池的数据源工厂
 * @author: panhongtong
 * @date: 2022/5/24 10:19
 **/
public class PooledDataSourceFactory extends UnPooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(props.getProperty("driver"));
        pooledDataSource.setUrl(props.getProperty("url"));
        pooledDataSource.setUsername(props.getProperty("username"));
        pooledDataSource.setPassword(props.getProperty("password"));
        return pooledDataSource;
    }

}
