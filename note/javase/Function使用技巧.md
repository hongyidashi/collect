# Function使用技巧

[TOC]

## 零、开篇

在开发过程中经常会使用`if...else...`进行判断抛出异常、分支处理等操作。这些`if...else...`充斥在代码中严重影响了代码代码的美观，这时我们可以利用`Java 8`的`Function`接口来消灭`if...else...`。

```java
if (...){
    throw new RuntimeException("出现异常了")；
} 

if (...){
    doSomething();
} else {
    doOther();
}
```

## 一、Java函数基本介绍

### 1. Function 函数式接口

使用注解`@FunctionalInterface`标识，并且只包含一个`抽象方法`的接口是`函数式接口`。`函数式接口`主要分为`Supplier`供给型函数、`Consumer`消费型函数、`Runnable`无参无返回型函数和`Function`有参有返回型函数。

```java
@FunctionalInterface
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of input to the {@code before} function, and to the
     *           composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Function)
     */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```



> `Function`可以看作转换型函数

### 2. Supplier供给型函数

`Supplier`的表现形式为不接受参数、只返回数据

```java
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```

### 3. Consumer消费型函数

`Consumer`消费型函数和`Supplier`刚好相反。`Consumer`接收一个参数，没有返回值

```java
@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

### 4. Runnable无参无返回型函数

`Runnable`的表现形式为即没有参数也没有返回值

```java
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
```

`Function`函数的表现形式为接收一个参数，并返回一个值。`Supplier`、`Consumer`和`Runnable`可以看作`Function`的一种特殊表现形式

## 二、使用小技巧

### 1. 处理抛出异常的if

1. 定义函数

定义一个抛出异常的形式的`函数式接口`, 这个接口只有参数没有返回值是个`供给型接口`

```java
/**
 * 描述: 抛异常接口
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:33
 **/
@FunctionalInterface
public interface ThrowExceptionFunction {

    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     **/
    void throwMessage(int code, String message);

}
```

2. 编写判断方法

创建工具类Function并创建一个`isThrow`方法，方法的返回值为刚才定义的`函数式接口`-`ThrowExceptionFunction`。`ThrowExceptionFunction`的接口实现逻辑为当参数`b`为`true`时抛出异常

```java
/**
 * 描述: Function工具类
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:38
 **/
public class FuncUtils {

    /**
     * 抛出异常
     * <p> 如果 b == true 则抛出异常
     * <p> 下面注释掉的方法跟这个等价，目的是方便不太熟悉lambda的同学对照
     *
     * @param b 是否要抛出异常
     * @return 函数式接口
     */
    public static ThrowExceptionFunction isThrow(boolean b) {
        return (code, message) -> {
            if (b) {
                throw new RuntimeException(message);
            }
        };
    }

//    public static ThrowExceptionFunction isThrow(boolean b) {
//        return new ThrowExceptionFunction() {
//            @Override
//            public void throwMessage(int code, String message) {
//                if (b) {
//                    throw new RuntimeException(message);
//                }
//            }
//        };
//    }

    private ExceptionUtils() {
        // private
    }

}
```

3. 测试

```java
public static void testEx() {
  FuncUtils.isThrow(false).throwMessage(-1000, "我不会出现，别等了");
  FuncUtils.isThrow(true).throwMessage(-2000, "我，如期而至");
}
```

结果很简单：

```
Exception in thread "main" java.lang.RuntimeException: 我，如期而至
	at com.collect.javase.func.ExceptionUtils.lambda$isThrow$0(ExceptionUtils.java:22)
	at com.collect.javase.func.TestFunc.testEx(TestFunc.java:17)
	at com.collect.javase.func.TestFunc.main(TestFunc.java:12)
```

### 2. 处理if分支操作

1. 定义函数式接口

创建一个名为`BranchHandle`的函数式接口，接口的参数为两个`Runnable`接口。这两个两个`Runnable`接口分别代表了为`true`或`false`时要进行的操作

```java
/**
 * 描述: 分支处理接口
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:51
 **/
@FunctionalInterface
public interface BranchHandle {

    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     **/
    void branch(Runnable trueHandle, Runnable falseHandle);

}
```

2. 编写判断方法

创建一个名为`condition`的方法，方法的返回值为刚才定义的`函数式接口`-`BranchHandle`。

```java
/**
 * 描述: Function工具类
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:38
 **/
public class FuncUtils {

    /**
     * 分支操作
     * <p> condition 用于确定执行哪个方法
     * <p> 下面注释掉的方法跟这个等价，目的是方便不太熟悉lambda的同学对照
     *
     * @param condition 条件
     * @return 函数式接口
     */
    public static BranchHandle condition(boolean condition) {
        return (trueHandle, falseHandle) -> {
            if (condition) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

//    public static BranchHandle condition(boolean conditio) {
//        return new BranchHandle() {
//            @Override
//            public void branch(Runnable trueHandle, Runnable falseHandle) {
//                if (conditio) {
//                    trueHandle.run();
//                } else {
//                    falseHandle.run();
//                }
//            }
//        };
//    }

    private FuncUtils() {
        // private
    }

}
```

3. 测试

```java
public static void testBranch() {
  FuncUtils.condition(true).branch(
    () -> System.out.println("诶嘿，如果是true就会执行到我"),
    () -> System.out.println("嘿哈，如果是true就会执行到我")
  );
}
```

### 3. 存在则消费

1. 定义函数

创建一个名为`PresentOrElseHandler`的函数式接口，接口的参数一个为`Consumer`接口。一个为`Runnable`,分别代表值不为空时执行消费操作和值为空时执行的其他操作

```java
/**
 * 描述: 非空消费处理器
 *
 * @author: panhongtong
 * @date: 2021-09-25 07:34
 **/
@FunctionalInterface
public interface PresentOrElseHandler<T extends Object> {

    /**
     * 值不为空时执行消费操作
     * 值为空时执行其他的操作
     *
     * @param action      值不为空时，执行的消费操作
     * @param emptyAction 值为空时，执行的操作
     **/
    void presentOrElseHandle(Consumer<? super T> action, Runnable emptyAction);

}
```

2. 编写判断方法

创建一个名为`isNotNull`的方法，方法的返回值为刚才定义的`函数式接口`-`PresentOrElseHandler`。

```java
/**
 * 描述: Function工具类
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:38
 **/
public class FuncUtils {

    /**
     * obj 不为空则消费，为空则执行其他操作
     *
     * @param obj 消费对象
     * @return 函数式接口
     **/
    public static PresentOrElseHandler<?> isNotNull(Object obj) {
        return (consumer, runnable) -> {
            if (obj == null) {
                runnable.run();
            } else {
                consumer.accept(obj);
            }
        };
    }
  
}
```

3. 测试

```java
public static void testPresent() {
  FuncUtils.isNotNull("大福").presentOrElseHandle(
    obj -> System.out.println(obj + "倒了(哭腔)"),
    () -> System.out.println("没有大福")
  );
}
```

