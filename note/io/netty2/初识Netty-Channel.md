# 初识Netty-Channel

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [一、channel的主要方法](#%E4%B8%80channel%E7%9A%84%E4%B8%BB%E8%A6%81%E6%96%B9%E6%B3%95)
- [二、什么是channelFuture？](#%E4%BA%8C%E4%BB%80%E4%B9%88%E6%98%AFchannelfuture)
- [三、什么是CloseFuture？](#%E4%B8%89%E4%BB%80%E4%B9%88%E6%98%AFclosefuture)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 一、channel的主要方法

1）close() 可以用来关闭 channel
2）closeFuture() 用来处理 channel 的关闭，有如下两种方式

> sync 方法作用是同步等待 channel 关闭 而 addListener 方法是异步等待 channel 关闭

3）pipeline() 方法添加处理器
4）write() 方法将数据写入
5）writeAndFlush() 方法将数据写入并刷出

## 二、什么是channelFuture？

我们看下面一段客户端代码，也是前面文章使用的代码

```java
    public static void main(String[]args)throws InterruptedException{
        Channel channel=new Bootstrap()
          .group(new NioEventLoopGroup(1))
          .handler(new ChannelInitializer<NioSocketChannel>(){
              @Override
              protected void initChannel(NioSocketChannel ch)throws Exception{
                  System.out.println("init...");
                  ch.pipeline().addLast(new StringEncoder());
                  }
              })
          .channel(NioSocketChannel.class)
          .connect("localhost",8080)
          .sync()
          .channel();

          channel.writeAndFlush("aaa");
          Thread.sleep(1000);
          channel.writeAndFlush("aaa");
    }
```

主要看到调用connect()方法除，此处返回的其实是一个ChannelFuture 对象，通过channel()方法可以获得channel对象。

```java
public ChannelFuture connect(String inetHost,int inetPort){
  return this.connect(InetSocketAddress.createUnresolved(inetHost,inetPort));
}
```

```java
public interface ChannelFuture extends Future<Void> {
    Channel channel();
    ......
}
```

需要注意的是，这个connect方法是一个异步的方法，调用过后实际并没有建立连接，所以我们得到的ChannelFuture对象中并不能立刻获得正确的channel。

通过下面的例子看一下现象，启动一个服务端，端口8080，这里不提供服务端代码了，使用前面的就行。启动我们写好的测试客户端代码：

```java
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: Channel
 * @date: 2022/4/26 10:05
 **/
public class ChannelFutureTest {

    public static void main(String[] args) throws Exception {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {

                    }
                })
                .connect("localhost", 8080);

        System.out.println(channelFuture.channel());

        //同步等待连接
        channelFuture.sync();
        System.out.println(channelFuture.channel());
    }
}
```

结果

```
[id: 0xb8c05c32]
[id: 0xb8c05c32, L:/127.0.0.1:51940 - R:localhost/127.0.0.1:8080]
```

如上结果所示，首先打印只有一个id地址，当执行sync()方法，此处会同步阻塞等待连接，如果一直无法连接会抛出超时异常。当成功建立连接后，会继续执行，并打印出如上结果最后一行的内容。

除使用sync()这个同步方法以外，还有一种异步的方式：

```java
// 异步
channelFuture.addListener((ChannelFutureListener)future->{
  System.out.println(future.channel());
});
```

ChannelFutureListener 会在连接建立时被调用（其中 operationComplete 方法），这里是一个函数式接口调用。

## 三、什么是CloseFuture？

我们同通过一段代码演示一下，此处涉及到channel的close方法，和CloseFuture的close方法。关闭是为了释放占用的资源。

看如下一段代码：

```java
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @description: CloseFuture
 * @author: panhongtong
 * @date: 2022/4/26 10:09
 **/
public class CloseFutureTest {
    public static void main(String[] args) throws Exception {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);

        // 同步等待连接
        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    System.out.println("关闭channel");
                    // close 异步操作 1s 之后
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        System.out.println("处理channel关闭后的操作");
    }
}
```

如上代码所示，我们的客户端，允许用户手动输入q进行关闭程序，否则就发送内容到服务端。

但是按照如上代码直接运行客户端，发现System.out.println("处理channel关闭后的操作");这条命令直接执行了，因为我们的主要关闭业务逻辑是启用子线程实现的。

也就是说我们在子线程，即channel还没有关闭就执行了代码，这样可能导致我们的业务逻辑存在问题。

所以我们需要在channel关闭后才进行打印，真实场景中就是channel关闭后进行剩余业务操作。

我们需要在 System.out.println("处理channel关闭后的操作"); 之前增加以下的代码：

```java
// 获取closefuture
ChannelFuture closeFuture=channel.closeFuture();
        //同步阻塞
        closeFuture.sync();
```

上述代码会获取到一个closeFuture对象，sync方法会同步阻塞在此，直到子线程当中的channel真正关闭了，才会继续执行代码。

输入1、2、3、q，直接看结果：

```
1
2
3
q
关闭channel
处理channel关闭后的操作
```

与channelFuture相同，closeFuture除了有sync方法进行同步阻塞，仍然也可以使用异步方式进行监听channel是否关闭的状态。

将 System.out.println("处理channel关闭后的操作"); 放在以下代码：

```java
closeFuture.addListener((ChannelFutureListener)future->System.out.println("处理channel关闭后的操作")); 
```

通过上面的代码我们已经能够成功监测到channel的关闭了，但是相信实践过朋友们会发现我们channel虽然关闭了，但是整个程序仍然在运行，整体的资源没有做到全部释放，这是应为EventLoopGroup当中的线程没有停止，这里需要引入一个方法：

> shutdownGracefully()

这个方式是EventLoopGroup当中的方法。我们需要做以下操作，为了大家看，我把所有的客户端内容全放在以下代码中了：

```java
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @description: CloseFuture
 * @author: panhongtong
 * @date: 2022/4/26 10:09
 **/
public class CloseFutureTest {
    public static void main(String[] args) throws Exception {
        // 将group提出来，不能匿名方式，为了后面调动shutdownGracefully()方法
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);

        // 同步等待连接
        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    System.out.println("关闭channel");
                    // close 异步操作 1s 之后
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();


        // 处理channel关闭后的操作
        // 获取 CloseFuture 对象， 1) 同步处理关闭， 2) 异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();

        //同步
        //closeFuture.sync();

        //异步 - EventLoopGroup线程未关闭
        //closeFuture.addListener((ChannelFutureListener) future -> System.out.println("处理channel关闭后的操作"));

        //异步 - EventLoopGroup线程优雅关闭
        closeFuture.addListener((ChannelFutureListener) future -> group.shutdownGracefully());
    }
}
```

结果：

```
1
2
3
q
关闭channel

Process finished with exit code 0
```
