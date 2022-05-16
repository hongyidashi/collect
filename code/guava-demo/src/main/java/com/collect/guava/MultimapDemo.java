package com.collect.guava;

import com.google.common.collect.ArrayListMultimap;

/**
 * @description: Multimap
 * @author: panhongtong
 * @date: 2022/5/16 21:48
 **/
public class MultimapDemo {

    public static void main(String[] args) {
        ArrayListMultimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("day", 1);
        multimap.put("day", 2);
        multimap.put("day", 8);
        multimap.put("month", 3);
        System.out.println(multimap);
        System.out.println(multimap.get("day"));
    }

}
