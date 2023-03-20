package com.collect.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: fastjson1 - JSONPath 测试
 * @author: panhongtong
 * @date: 2023/3/17 14:17
 **/
public class JSONPathTest {

    public static void main(String[] args) {
//        getSingleData();
//        getListData();
        filter();
//        other();
    }

    static void getSingleData() {
        // 可以通过 [$. + 字段名]获取到对应值
        System.out.println("eval name:" + JSONPath.eval(PERSON, "$.name"));
        System.out.println("eval age:" + JSONPath.eval(PERSON, "$.age"));

        // 与Eval效果一致，据说性能会更高
        System.out.println("extract name:" + JSONPath.extract(PERSON_JSON, "$.name"));
        System.out.println("extract age:" + JSONPath.extract(PERSON_JSON, "$.age"));
    }

    static void getListData() {
        // 获取某个元素 语法: [元素下标]
        System.out.println("获取0号元素:" + JSONPath.eval(PEOPLE, "[0]"));

        // 获取集合中某个元素的字段 语法:
        // [元素下标].字段名
        // 等价 [元素下标]['字段名'] 此处'不可省略
        System.out.println("获取0号元素name:" + JSONPath.eval(PEOPLE, "[0].name"));
        System.out.println("获取0号元素name:" + JSONPath.eval(PEOPLE, "[0]['name']"));
        System.out.println("获取0号元素age:" + JSONPath.eval(PEOPLE, "[0].age"));

        // 获取对象的非空属性名称 语法: [元素下标].keySet() 注意，此处只能获取非空字段名
        System.out.println("获取元素所有字段名1: " + JSONPath.eval(PEOPLE, "[0].keySet()"));
        System.out.println("获取元素所有字段名2: " + JSONPath.eval(PEOPLE, "$[3].keySet()"));

        // 多个属性访问 语法: ['字段名1','字段名2']
        // 返回结果为多个集合，且会略过为null的值: [[字段1值集合], [字段2值集合]]
        System.out.println("获取元素name和age: " + JSONPath.eval(PEOPLE, "['name','age']"));
        System.out.println("获取元素children: " + JSONPath.eval(PEOPLE, "['children']"));

        // 对象的所有属性
        System.out.println("获取元素所有属性1: " + JSONPath.eval(PEOPLE, "$.*"));
        System.out.println("获取元素所有属性2: " + JSONPath.eval(PEOPLE, "$"));

        // 获取集合长度
        System.out.println("获取元素个数1: " + JSONPath.eval(PEOPLE, "$.size()"));
        System.out.println("获取元素个数2: " + JSONPath.eval(PEOPLE, "$.length()"));

        // 获取最后一个元素
        System.out.println("获取最后一个元素: " + JSONPath.eval(PEOPLE, "[-1]"));

        // 获取指定元素
        System.out.println("获取0和3号元素name: " + JSONPath.eval(PEOPLE, "[0,3].name"));

        // 获取所有元素指定字段
        System.out.println("获取所有元素name: " + JSONPath.eval(PEOPLE, "$.name"));

        // 范围访问
        System.out.println("获取0~3号元素name: " + JSONPath.eval(PEOPLE, "[0:3].name"));

        // 范围访问，指定步长
        System.out.println("指定步长为2获取元素name: " + JSONPath.eval(PEOPLE, "[:-1:2].name"));
    }

    static void filter() {
        // 对象属性非空过滤 语法: [?(字段名)]
        System.out.println("获取children字段不为空的name: " + JSONPath.eval(PEOPLE, "[?(children)]['name','children']"));

        // 数值类型对象属性比较过滤 语法: [字段名 =,!=,>,>=,<,<= 比较值]
        System.out.println("获取age大于3的元素name和age: " + JSONPath.eval(PEOPLE, "[age>=3]['name','age']"));

        // IN过滤 语法: [字段名 in ('值1', '值2')]
        System.out.println("获取name为'张三'和'王五'的元素: " + JSONPath.eval(PEOPLE, "[name in ('张三','王五')]"));

        // 比较和IN组合使用
        System.out.println("获取name为'张三'和'王五'且age>=5的元素: " + JSONPath.eval(PEOPLE, "[name in ('张三','王五')][age>=5]"));

        // 字符串类型like过滤 语法: [字段名 like '值%']
        // 支持not like，通配符只支持%
        System.out.println("获取name中包含'二'的元素: " + JSONPath.eval(PEOPLE, "[name like '%二%'].name"));

        // BETWEEN过滤, 支持数值类型，支持not between， 语法: [字段名 between 字段值1 and 字段值2]
        System.out.println("获取age在2~3的元素: " + JSONPath.eval(PEOPLE, "[age between 2 and 3]"));
    }

    static void other() {
        // 计算Size
        System.out.println("获取元素个数: " + JSONPath.size(PEOPLE, "$"));

        // 是否包含，path中是否存在对象
        System.out.println("是否存在对象 有值: " + JSONPath.contains(PEOPLE, "$"));
        System.out.println("是否存在对象 null: " + JSONPath.contains(null, "$"));
        System.out.println("是否存在对象 空集合: " + JSONPath.contains(new ArrayList<>(), "$"));

        // new Person("刘一", 1)
        Person person1 = new Person("刘一", 1);
        Person person2 = new Person("刘二", 1);
        System.out.println("是否包含 person1: " + JSONPath.containsValue(PEOPLE, "$", person1));
        System.out.println("是否包含 person2: " + JSONPath.containsValue(PEOPLE, "$", person2));

        // 修改制定路径的值，如果修改成功，返回true，否则返回false
        System.out.println("刘一替换为刘二前的数组: " + JSONPath.eval(PEOPLE, "[0]"));
        System.out.println("刘一替换为刘二结果: " + JSONPath.set(PEOPLE, "[0]", person2));
        System.out.println("刘一替换为刘二后的数组: " + JSONPath.eval(PEOPLE, "[0]"));

        // 在数组或者集合中添加元素
        System.out.println("添加元素前数组: " + JSONPath.eval(PEOPLE, "$.name"));
        JSONPath.arrayAdd(PEOPLE, "$", person1, person1);
        System.out.println("添加元素后数组: " + JSONPath.eval(PEOPLE, "$.name"));
    }

    private static final Person PERSON;

    private static final List<Person> PEOPLE = new ArrayList<>();

    private static final String PERSON_JSON;

    static {
        List<Person> children = new ArrayList<>();
        children.add(new Person("小福", 8));
        PERSON = new Person("大福", 18, children);
        PERSON_JSON = JSON.toJSONString(PERSON);


        List<Person> children1 = new ArrayList<>();
        List<Person> children2 = new ArrayList<>();
        children1.add(new Person("孙七", 7));
        children1.add(new Person("周八", 8));
        children2.add(new Person("吴九", 9));

        PEOPLE.add(new Person("刘一", 1));
        PEOPLE.add(new Person("陈二", 2));
        PEOPLE.add(new Person("张三", 3));
        PEOPLE.add(new Person("李四", 4, children1));
        PEOPLE.add(new Person("王五", 5));
        PEOPLE.add(new Person("王小二", 5));
        PEOPLE.add(new Person("赵六", 6, children2));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Person {
        private String name;
        private Integer age;
        private List<Person> children;

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

}
