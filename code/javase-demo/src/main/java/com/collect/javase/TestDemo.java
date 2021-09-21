package com.collect.javase;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-09-16 22:42
 **/
public class TestDemo {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(13);
        System.out.println(map.size());
        map.put("大福", "大傻子");
        System.out.println(map.size());
    }
}
