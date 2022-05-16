# Guava工具类-Map

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

    - [零、开篇](#%E9%9B%B6%E5%BC%80%E7%AF%87)
    - [一、Table：双键 Map](#%E4%B8%80table%E5%8F%8C%E9%94%AE-map)
    - [二、BiMap：双向 Map](#%E4%BA%8Cbimap%E5%8F%8C%E5%90%91-map)
    - [三、Multimap：多值 Map](#%E4%B8%89multimap%E5%A4%9A%E5%80%BC-map)
    - [四、RangeMap：范围 Map](#%E5%9B%9Brangemap%E8%8C%83%E5%9B%B4-map)
    - [五、ClassToInstanceMap：实例 Map](#%E4%BA%94classtoinstancemap%E5%AE%9E%E4%BE%8B-map)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 零、开篇

Guava 是 Google 公司开发的一款 Java 类库扩展工具包，内含了丰富的 API，涵盖了集合、缓存、并发、I/O 等多个方面。

使用这些 API 一方面可以简化我们代码，使代码更为优雅，另一方面它补充了很多 jdk 中没有的功能，能让我们开发中更为高效。

引入依赖开始：

```xml

<dependencies>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>30.1.1-jre</version>
    </dependency>
</dependencies>
```

## 一、Table：双键 Map

Java 中的 Map 只允许有一个 key 和一个 value 存在，但是 guava 中的 Table 允许一个 value 存在两个 key。

直接上栗子：

```java
import com.google.common.collect.HashBasedTable;

import java.util.Map;

/**
 * @description: table
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
```

输出结果如下：

```
{春节假期={大福=3, 二福=4, 三福=5}, 年假={大福=3, 二福=4, 三福=5}}
{大福=3, 二福=4, 三福=5}
{春节假期=3, 年假=3}
```

## 二、BiMap：双向 Map

在普通 Map 中，如果要想根据 value 查找对应的 key，没什么简便的办法，无论是使用 for 循环还是迭代器，都需要遍历整个 Map。

依旧是直接上栗子：

```java
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @description: BiMap
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
```

执行结果如下：

```
大福
1
{二福=2, 小福=3, 大福=4}
{2=二福, 3=小福, 4=大福}
{二福=2, 大福=3}
```

## 三、Multimap：多值 Map

Java 中的 Map 维护的是键值一对一的关系，如果要将一个键映射到多个值上，那么就只能把值的内容设为集合形式，简单实现如下：

```java
Map<String, List<Integer>>map=new HashMap<>();
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        map.put("day",list);
```

Guava 中的 Multimap 提供了将一个键映射到多个值的形式，使用起来无需定义复杂的内层集合，可以像使用普通的 Map 一样使用它，定义及放入数据如下：

```java
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * @description: Multimap
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
```

执行结果如下：

```
{month=[3], day=[1, 2, 8]}
[1, 2, 8]
```

## 四、RangeMap：范围 Map

先看一个例子，假设我们要根据分数对考试成绩进行分类，那么代码中就会出现这样丑陋的 if-else：

```java
public static String getRank(int score){
        if(0<=score&&score<60)
        return"fail";
        else if(60<=score&&score<=90)
        return"satisfactory";
        else if(90<score &&score<=100)
        return"excellent";
        return null;
        }
```

而 guava 中的 RangeMap 描述了一种从区间到特定值的映射关系，让我们能够以更为优雅的方法来书写代码。

下面用 RangeMap 改造上面的代码并进行测试：

```java
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

/**
 * @description: RangeMap
 * @date: 2022/5/16 21:52
 **/
public class RangeMapDemo {

    public static void main(String[] args) {
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closedOpen(0, 60), "fail");
        rangeMap.put(Range.closed(60, 90), "satisfactory");
        rangeMap.put(Range.openClosed(90, 100), "excellent");

        System.out.println(rangeMap.get(59));
        System.out.println(rangeMap.get(60));
        System.out.println(rangeMap.get(90));
        System.out.println(rangeMap.get(91));
    }

}
```

在上面的代码中，先后创建了 [0,60) 的左闭右开区间、[60,90] 的闭区间、(90,100] 的左开右闭区间，并分别映射到某个值上。

运行结果打印：

```
fail
satisfactory
satisfactory
excellent
```

当然我们也可以移除一段空间，下面的代码移除了 [70,80] 这一闭区间后，再次执行 get 时返回结果为 null：

```java
rangeMap.remove(Range.closed(70,80));
        System.out.println(rangeMap.get(75));
```

## 五、ClassToInstanceMap：实例 Map

ClassToInstanceMap 是一个比较特殊的 Map，它的键是 Class，而值是这个 Class 对应的实例对象。

先看一个简单使用的例子，使用 putInstance 方法存入对象：

```
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
        classToInstanceMap.put(Person.class, new Person("person"));
        classToInstanceMap.put(Dept.class, new Dept("dept"));

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
```

执行结果：

```
-991716492
-991716492
```

那么，使用 ClassToInstanceMap 这种方式有什么好处呢？

首先，这里最明显的就是在取出对象时省去了复杂的强制类型转换，避免了手动进行类型转换的错误。

其次，我们可以看一下 ClassToInstanceMap 接口的定义，它是带有泛型的：

```java
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B> {
}
```

