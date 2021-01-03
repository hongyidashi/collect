# 序列化

目录
+ [Java序列化和反序列化的概念](#Java序列化和反序列化的概念)
+ [如何实现Java序列化](#如何实现Java序列化)


## Java序列化和反序列化的概念
- 序列化：把对象转换为字节序列的过程称为对象的序列化；
- 反序列化：把字节序列恢复为对象的过程称为对象的反序列化。

就是把内存里面的这些对象给变成一连串的字节描述的过程。  
常见的就是变成文件。

**什么情况下需要Java序列化**  
- 当你想把的内存中的对象状态保存到一个文件中或者数据库中时候；
- 当你想用套接字在网络上传送对象的时候；
- 当你想通过RMI传输对象的时候；

## 如何实现Java序列化  
实现Serializable接口即可

```java
public class GirlFriend implements Serializable {

    private static String AGE = "18";
    private String name;
    private String size;
    transient private String car;

    //    private String house;
    //
    //    public String getHouse() {
    //        return house;
    //    }
    //
    //    public void setHouse(String house) {
    //        this.house = house;
    //    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "GirlFriend{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", car='" + car + '\'' +
                ", AGE='" + AGE + '\'' +
                '}';
    }
}
```

下面是测试的main方法：
```java
public class SerializableTest {
    public static void main(String[] args) throws Exception {
        serializeFlyPig();
        GirlFriend girlFriend = deserializeFlyPig();
        System.out.println(girlFriend);
    }

    /**
     * 序列化
     */
    private static void serializeFlyPig() throws Exception {
        GirlFriend girlFriend = new GirlFriend();
        girlFriend.setSize("36E");
        girlFriend.setName("大福");
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
```

运行上面代码，从结果来看：
1. 他实现了对象的序列化和反序列化；
2. transient 修饰的属性，是不会被序列化的；

然后，**静态的属性是不能被序列化和反序列化的**，可这样验证：
先序列化一次
```java
public static void main(String[] args) throws Exception {
        serializeFlyPig();
        //GirlFriend girlFriend = deserializeFlyPig();
        //System.out.println(girlFriend);
    }
```
序列化后将 AGE 改为 88，再执行反序列化，如果静态属性可以被序列化则 AGE 字段应该为 18 才对，然鹅。。(自己测)

### serialVersionUID 的作用和用法
先是单独执行序列化方法，生成文件；然后，打开属性 house ，这之后，再次执行反序列化方法，看现象。  
抛异常：InvalidClassException：
```
Exception in thread "main" java.io.InvalidClassException: com.collect.javase.serial.GirlFriend; local class incompatible: stream classdesc serialVersionUID = 8914124313756399373, local class serialVersionUID = 8174368720183018360
	at java.io.ObjectStreamClass.initNonProxy(ObjectStreamClass.java:699)
	at java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1885)
	at java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1751)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2042)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1573)
	at java.io.ObjectInputStream.readObject(ObjectInputStream.java:431)
	at com.collect.javase.serial.SerializableTest.deserializeFlyPig(SerializableTest.java:37)
	at com.collect.javase.serial.SerializableTest.main(SerializableTest.java:13)
```
解释一下：  
因为我们在model里面是没有明确的给这个 serialVersionUID 赋值，但是，Java会自动的给我赋值的，这个值跟这个model的属性相关计算出来的。

序列化的时候，那时候还没有这个 house 属性呢，所以，自动生成的 serialVersionUID 这个值，在我反序列化的时候Java自动生成的这个serialVersionUID值是不同的，它就抛异常啦。

如果我们一开始就指定这个值，就可以正常序列化和反序列化。

**这个值对我们有什么意义**    
如果定义bean的时候没写这个 serialVersionUID ，那么在后来扩展的时候，可能就会出现不认识旧数据的bug，那不就炸啦吗？

所以，有这么个理论，就是在实现这个Serializable 接口的时候，一定要给这个 serialVersionUID 赋值，就是这么个问题。
