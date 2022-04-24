package com.collect.netty.nio;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description: Path和Paths演示
 * @author: panhongtong
 * @date: 2022/4/24 09:20
 **/
public class PathDemo {

    public static void main(String[] args) {
        Path path = Paths.get("/Users/panhongtong/mydir/../text.txt");
        System.out.println(path);
        // 正常化路径
        System.out.println(path.normalize());
    }

}
