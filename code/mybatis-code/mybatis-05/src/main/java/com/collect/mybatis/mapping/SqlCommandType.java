package com.collect.mybatis.mapping;

/**
 * @description: SQL 指令类型
 * @author: panhongtong
 * @date: 2022/5/20 09:01
 **/
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;

}
