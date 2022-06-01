package com.collect.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @description: 结果集处理器
 * @author: panhongtong
 * @date: 2022/5/26 14:29
 **/
public interface ResultSetHandler {

    /**
     * 处理结果集
     *
     * @param statement statement
     * @param <E>       元素类型
     * @return 处理结果集
     * @throws SQLException e
     */
    <E> List<E> handleResultSets(Statement statement) throws SQLException;

}
