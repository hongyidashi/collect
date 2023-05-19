package com.collect.sharding.mapper;

import com.collect.sharding.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入
     *
     * @param order
     */
    void insert(Order order);

    /**
     * 批量查询
     *
     * @param idList
     * @return
     */
    List<Order> selectListByIds(@Param("idList") List<Long> idList);
}
