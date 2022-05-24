package com.collect.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: 数据源工厂
 * @author: panhongtong
 * @date: 2022/5/22 10:46
 **/
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();

}
