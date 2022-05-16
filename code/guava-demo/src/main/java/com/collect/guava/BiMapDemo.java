package com.collect.guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @description: BiMap
 * @author: panhongtong
 * @date: 2022/5/16 21:39
 **/
public class BiMapDemo {

    public static void main(String[] args) {
        HashBiMap<Integer, String> biMap = HashBiMap.create();
        biMap.put(1, "大福");
        biMap.put(2, "二福");
        biMap.put(3, "小福");

        // 正常使用
        System.out.println(biMap.get(1));

        // 使用value获取key
        BiMap<String, Integer> inverse = biMap.inverse();
        System.out.println(inverse.get("大福"));

        // inverse并不是一个新的map，而是一个视图，修改它会影响到原来的map
        inverse.put("大福", 4);
        System.out.println(inverse);
        System.out.println(biMap);

        // 且 value不可重复，重复会报错
        // inverse.put("大福", 3);

        // 强制替换
        inverse.forcePut("大福", 3);
        System.out.println(inverse);
    }

}
