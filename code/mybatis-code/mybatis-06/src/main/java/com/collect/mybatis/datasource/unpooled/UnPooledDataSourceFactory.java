package com.collect.mybatis.datasource.unpooled;

import com.collect.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: 无池化数据源工厂
 * @author: panhongtong
 * @date: 2022/5/22 11:12
 **/
public class UnPooledDataSourceFactory implements DataSourceFactory {

    protected Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        UnPooledDataSource unpooledDataSource = new UnPooledDataSource();
        unpooledDataSource.setDriver(props.getProperty("driver"));
        unpooledDataSource.setUrl(props.getProperty("url"));
        unpooledDataSource.setUsername(props.getProperty("username"));
        unpooledDataSource.setPassword(props.getProperty("password"));
        return unpooledDataSource;
    }

}
