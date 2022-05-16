package com.collect.guava;

import com.collect.guava.bean.Dept;
import com.collect.guava.bean.Person;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description: ClassToInstanceMap
 * @author: panhongtong
 * @date: 2022/5/16 21:56
 **/
public class ClassToInstanceMapDemo {

    public static void main(String[] args) {
        MutableClassToInstanceMap<Object> classToInstanceMap = MutableClassToInstanceMap.create();
        Person p = new Person("person");
        classToInstanceMap.putInstance(Person.class, new Person("person"));
        classToInstanceMap.putInstance(Dept.class, new Dept("dept"));

        Person person = classToInstanceMap.getInstance(Person.class);
        System.out.println(person.hashCode());
        System.out.println(p.hashCode());

        // 这个泛型同样可以起到对类型进行约束的作用，value 要符合 key 所对应的类型
        ClassToInstanceMap<Map> instanceMap = MutableClassToInstanceMap.create();
        HashMap<String, Object> hashMap = new HashMap<>();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        ArrayList<Object> list = new ArrayList<>();

        instanceMap.putInstance(HashMap.class, hashMap);
        instanceMap.putInstance(TreeMap.class, treeMap);
        // 这里会报错
        // instanceMap.putInstance(ArrayList.class, list);
    }

}
