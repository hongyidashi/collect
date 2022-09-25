package com.collect.javase.love;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 表白代码
 * @author: panhongtong
 * @date: 2022/9/21 21:01
 **/
public class Love {

    static List<Persion> world = new ArrayList<>();
    static Persion me = new Persion();

    public static void main(String[] args) {
        // 遍历整个世界，只为找到你
        world.forEach(persion -> {
            Persion zjl = persion;
            // 当我发现你是我的真爱时
            if (zjl.lover == "MyLove") {
                // 那么我的心将属于你
                me.myHeart = "You";
                // 你将成为我的妻子
                me.wife = zjl;
            }
        });
    }

}
