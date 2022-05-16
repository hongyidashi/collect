package com.collect.guava;

import com.google.common.collect.HashBasedTable;

import java.util.Map;

/**
 * @description: table
 * @author: panhongtong
 * @date: 2022/5/13 15:26
 **/
public class TableDemo {

    public static void main(String[] args) {
        HashBasedTable<String, String, Integer> table = HashBasedTable.create();
        table.put("春节假期", "大福", 3);
        table.put("年假", "大福", 3);
        table.put("春节假期", "二福", 4);
        table.put("年假", "二福", 4);
        table.put("春节假期", "三福", 5);
        table.put("年假", "三福", 5);
        Map<String, Map<String, Integer>> rowMap = table.rowMap();
        Map<String, Map<String, Integer>> columnMap = table.columnMap();
        System.out.println(table);
        System.out.println(rowMap.get("春节假期"));
        System.out.println(columnMap.get("大福"));
    }

}
