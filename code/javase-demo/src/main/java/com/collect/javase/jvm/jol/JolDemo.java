package com.collect.javase.jvm.jol;

import org.openjdk.jol.vm.VM;

/**
 * 描述: jol 使用示例
 *
 * @author: panhongtong
 * @date: 2021-05-05 09:43
 **/
public class JolDemo {
    public static void main(String[] args) {
        // 查看JVM
        System.out.println(VM.current().details());
    }
}
