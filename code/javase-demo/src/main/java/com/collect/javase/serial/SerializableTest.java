package com.collect.javase.serial;

import java.io.*;

/**
 * 描述: 序列化测试
 * 作者: panhongtong
 * 创建时间: 2021-01-03 22:23
 **/
public class SerializableTest {
    public static void main(String[] args) throws Exception {
//        serializeFlyPig();
        GirlFriend girlFriend = deserializeFlyPig();
        System.out.println(girlFriend);
    }

    /**
     * 序列化
     */
    private static void serializeFlyPig() throws Exception {
        GirlFriend girlFriend = new GirlFriend();
        girlFriend.setSize("36E");
        girlFriend.setCar("二手奥拓");
        girlFriend.setName("断腿少女");
        // ObjectOutputStream 对象输出流，将 flyPig 对象存储到 flyPig.txt 文件中，完成对 flyPig 对象的序列化操作
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/Users/panhongtong/work/IDEA-workspace/collect/code/javase-demo/src/main/java/com/collect/javase/serial/girlFriend.txt")));
        oos.writeObject(girlFriend);
        System.out.println("GirlFriend 对象序列化成功！");
        oos.close();
    }

    /**
     * 反序列化
     */
    private static GirlFriend deserializeFlyPig() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("/Users/panhongtong/work/IDEA-workspace/collect/code/javase-demo/src/main/java/com/collect/javase/serial/girlFriend.txt")));
        GirlFriend girlFriend = (GirlFriend) ois.readObject();
        System.out.println("GirlFriend 对象反序列化成功！");
        return girlFriend;
    }
}
