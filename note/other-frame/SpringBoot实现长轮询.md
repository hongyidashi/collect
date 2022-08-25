# SpringBoot实现长轮询

[TOC]

## 一、长轮询

长轮询简单来说就是：是一种实现实时通信的技术，客户端向服务端发起http请求，服务端先把请求hold住，等到特定时机再给客户端响应，否则给予超时响应。

常见的场景：如一个页面要实时展示新闻，客户端就会一直发请求到服务端获取最新的新闻信息；但新闻不是总是有更新的，但又需要有新闻时立刻展示到页面上，此时就可以用的长轮询。客户端发送获取最新新闻的请求到服务端，服务端收到请求hold住，请求超时时间设置为30秒，此期间内若有新的新闻就将新闻塞到请求的响应返回给客户端，若没有则超时返回空对象；客户端接收到响应后立即发送下一个请求到服务端，以此往复。

适用场景：长轮询在处理一些简单信息传递，不频繁的应用中比较常用。但对于一些实时传递要求比较高的应用来说，比如在线游戏，在线证券，在线聊天，在线新闻播报等应用来说就显得力不从心，一来要处理实时信息，二来要在极短的时间内处理大量的数据，此时就要用的长连接websocket。

## 二、Servlet异步处理实现长轮询

### 0. [我的项目代码](https://gitee.com/hongyidashi/giants/tree/2.0.0/giants-framework/giants-web/src/main/java/org/giants/web/longpoll)

### 1. 基本原理

#### 同步&异步

用Servlet实现长轮询，可以使用同步Servlet实现，也可以使用异步Servlet实现，二者的区别：

同步：占用了容器线程(如tomcat线程)，如果容器只有200个线程，那么长轮询最大支持请求个数就是200个，如果线程被占满，会严重影响同系统其他业务；

异步：不占用容器线程。

#### Servlet 3.0

在Servlet 3.0之前，Servlet采用Thread-Per-Request的方式处理请求，即每一次Http请求都由某一个线程从头到尾负责处理。如果一个请求需要进行IO操作，比如访问数据库、调用第三方服务接口等，那么其所对应的线程将同步地等待IO操作完成， 而IO操作是非常慢的，所以此时的线程并不能及时地释放回线程池以供后续使用。在并发量越来越大的情况下，这将带来严重的性能问题。即便是像Spring之类的高层框架也脱离不了这样的桎梏，因为他们都是建立在Servlet之上的。为了解决这样的问题，Servlet 3.0引入了异步处理，然后在Servlet 3.1中又引入了非阻塞IO来进一步增强异步处理的性能。

Servlet 3.0 开始提供了AsyncContext用来支持异步处理请求，使用AsyncContext，我们就可以将耗时的操作交给另一个thread去做，这样HTTP thread就被释放出来了，可以去处理其他请求了。

因此，使用Servlet异步实现长轮询，是与其他框架(如SpringMvc等)无关的，是Servlet自己本身就有的功能。

### 2. 代码实现

#### 0. 大体思路

客户端发起请求，请求体中标明关注的事件，若该事件有变化，则立即响应客户端，否则等待超时响应。

接口规划如下：

1. 监听(客户端请求获取数据)接口
2. 触发接口(用于触发事件)

代码实现里用到的一些类，如R<?>、GiantsException等是我自定义的统一返回和异常，根据需要改成自己的即可。

#### 1. 基于springboot-web创建一个工程

这个没啥好说的，基于springboot快速创建工程

#### 2. 长轮询请求接口

直接上代码

```java
import cn.hutool.core.util.IdUtil;
import org.giants.core.bean.R;
import org.giants.web.longpoll.servlet.LongPollAsyncListener;
import org.giants.web.longpoll.servlet.ServletLongPollReq;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 长轮询rest
 * @author: panhongtong
 * @date: 2022/8/19 16:35
 **/
@RestController
@RequestMapping("/longPoll")
public class LongPollRest {

    /**
     * 获取数据 servlet方式
     *
     * @param req 请求对象
     */
    @PostMapping("/servlet/get")
    public void get(@RequestBody @Validated ServletLongPollReq req,
                    HttpServletRequest request) {
        // 为每一个请求设置一个reqId，用于标识一个请求
        req.setReqId(IdUtil.nanoId());
        // 开启异步支持
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        // 获取AsyncContext
        AsyncContext asyncContext = request.startAsync();
        // 将请求体放到Attribute中，方便后面取用
        request.setAttribute(ServletLongPollReq.ATTRIBUTE_NAME, req);
        // 添加监听器
        asyncContext.addListener(new LongPollAsyncListener());
      	// 设置请求超时时间，超时时会触发监听器里的onTimout()方法
        asyncContext.setTimeout(req.getTimeoutUint().toMillis(req.getTimeout()));
        req.setAsyncContext(asyncContext);

        // 添加到队列
        LongPollManager.addReq(req);
    }
  
  	/**
     * 触发响应
     *
     * @param trigger 触发对象
     * @param <T>     数据类型
     */
    @PostMapping("/trigger")
    public <T> void trigger(@RequestBody @Validated LongPollTrigger<T> trigger) {
        LongPollManager.onEvent(trigger);
    }

}
```

请求体ServletLongPollReq代码如下：

```java
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * @description: 基类长轮询请求
 * @author: panhongtong
 * @date: 2022/8/16 23:04
 **/
@Data
public class BasicLongPollReq {

    /**
     * 请求ID
     */
    private String reqId;

    /**
     * 超时时间(默认问29000)
     */
    private Long timeout = 29000L;

    /**
     * 超时时间单位(默认为毫秒)
     */
    private TimeUnit timeoutUint = TimeUnit.MILLISECONDS;

    /**
     * 业务字段(根据需要添加)
     */
    private String businessKey;

    /**
     * 感兴趣的事件类型
     */
    @NotBlank(message = "感兴趣的事件类型 不能为空")
    private String notifyEvent;

}
```

```java
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.AsyncContext;

/**
 * @description: Servlet 长轮询请求对象
 * @author: panhongtong
 * @date: 2022/8/24 17:37
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ServletLongPollReq extends BasicLongPollReq {

    /**
     * req attribute name
     */
    public static final String ATTRIBUTE_NAME = "longPollReq";

    /**
     * 异步上下文
     */
    private AsyncContext asyncContext;

}
```

#### 3. 配置异步监听器

异步监听器可以监听请求的各种时间，我们需要用它来实现超时返回

```java
import lombok.extern.slf4j.Slf4j;
import org.giants.web.longpoll.LongPollManager;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Objects;

/**
 * @description: 长轮询一般监听器
 * @author: panhongtong
 * @date: 2022/8/19 15:13
 **/
@WebListener
@Slf4j
public class LongPollAsyncListener implements AsyncListener {

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        log.info("[LongPollAsyncListener]onComplete...");
    }

    /**
     * 超时触发
     *
     * @param asyncEvent 异步事件
     * @throws IOException e
     */
    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        log.info("[LongPollAsyncListener]onTimeout...");
        AsyncContext asyncContext = asyncEvent.getAsyncContext();
        // 获取请求体
        ServletLongPollReq req = (ServletLongPollReq) asyncContext.getRequest()
                .getAttribute(ServletLongPollReq.ATTRIBUTE_NAME);
        if (Objects.nonNull(req)) {
            LongPollManager.onEvent(req.getNotifyEvent(), req.getReqId(), null);
        }
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        log.info("[LongPollAsyncListener]onError...");
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        log.info("[LongPollAsyncListener]onStartAsync...");
    }
}
```

#### 4. 长轮询请求管理器

用于管理所有的长轮询请求，如将请求添加到队列，分发处理请求等逻辑

```java
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.giants.core.constant.enums.WebStatus;
import org.giants.core.exception.GiantsException;
import org.springframework.beans.factory.BeanCreationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 长轮询请求管理器
 * @author: panhongtong
 * @date: 2022/8/16 23:15
 **/
@Slf4j
public class LongPollManager {

    /**
     * Map<感兴趣的事件, Map<请求ID,长轮询请求>>
     * <p> 请求队列，这个设计可以方便通过 事件+请求ID来找到请求
     * <p> 事件接收到数据时，要通知所有请求
     * <p> 请求超时时，要获取到某个请求，只回复某个请求
     */
    private static final Map<String, ConcurrentHashMap<String, BasicLongPollReq>> REQ_MAP = new ConcurrentHashMap<>();

    /**
     * Map<感兴趣的事件, 事件处理器>
     * <p> 用于获取事件对呀的处理器
     */
    private static final Map<String, LongPollProcessor> HANDLER_MAP = new HashMap<>();

    /**
     * 添加长轮询请求到队列
     *
     * @param req req
     */
    public static void addReq(BasicLongPollReq req) {
        String notifyEvent = req.getNotifyEvent();

        // 获取map，若不存在则创建一个
        ConcurrentHashMap<String, BasicLongPollReq> reqMap = REQ_MAP.computeIfAbsent(notifyEvent, event -> {
            ConcurrentHashMap<String, BasicLongPollReq> map = new ConcurrentHashMap<>(16);
            log.info("[LongPollManager][addReq]创建事件[{}]的请求Map", notifyEvent);
            return map;
        });

        reqMap.put(req.getReqId(), req);
    }

    /**
     * 处理长轮询请求
     *
     * @param trigger 触发对象
     * @param <T>     数据泛型
     */
    public static <T> void onEvent(LongPollTrigger<T> trigger) {
        if (Objects.isNull(trigger)) {
            throw new GiantsException(WebStatus.PARAMS_ERROR, "触发对象为空，onEvent失败");
        }
        onEvent(trigger.getNotifyEvent(), trigger.getData());
    }

    /**
     * 处理长轮询请求
     *
     * @param notifyEvent 事件
     * @param data        数据 数据为空表示超时或没有数据
     * @param <T>         数据泛型
     */
    public static <T> void onEvent(String notifyEvent, T data) {
        onEvent(notifyEvent, null, data);
    }

    /**
     * 处理长轮询请求
     *
     * @param notifyEvent 事件
     * @param reqId       要处理的请求ID 指定了reqId则只处理指定的reqId；否则处理全部
     * @param data        数据 数据为空表示超时或没有数据
     * @param <T>         数据泛型
     */
    public static <T> void onEvent(String notifyEvent, String reqId, T data) {
        if (CharSequenceUtil.isBlank(notifyEvent)) {
            throw new GiantsException(WebStatus.PARAMS_ERROR, "事件为空，onEvent失败");
        }

        LongPollProcessor processor = HANDLER_MAP.get(notifyEvent);
        if (Objects.isNull(processor)) {
            // 没有定义处理器
            log.error("[LongPollManager][onEvent]事件[{}]没有定义处理器", notifyEvent);
            return;
        }

        ConcurrentHashMap<String, BasicLongPollReq> reqMap = REQ_MAP.get(notifyEvent);
        if (CollUtil.isEmpty(reqMap)) {
            // 没有被订阅
            log.error("[LongPollManager][onEvent]事件[{}]没有订阅者", notifyEvent);
            return;
        }

        // 处理 指定了reqId则只处理指定的reqId；否则处理全部
        if (CharSequenceUtil.isNotBlank(reqId) && Objects.nonNull(reqMap.get(reqId))) {
            BasicLongPollReq req = reqMap.remove(reqId);
            processor.process(req, data);
        } else {
            for (BasicLongPollReq req : reqMap.values()) {
                processor.process(req, data);
                reqMap.remove(req.getReqId());
            }
        }

    }

    /**
     * 添加处理器
     *
     * @param notifyEvent 感兴趣事件
     * @param handler     处理器
     */
    public static void addHandler(String notifyEvent, LongPollProcessor handler) {
        if (Objects.isNull(handler)) {
            return;
        }

        LongPollProcessor processor = HANDLER_MAP.get(notifyEvent);
        if (Objects.nonNull(processor)) {
            String exMsg = String.format("消息类型[%s]存在两个相同的处理器:[%s][%s]",
                    notifyEvent, processor.getClass(), handler.getClass());
            throw new BeanCreationException(exMsg);
        }

        HANDLER_MAP.put(notifyEvent, handler);
        log.info("[LongPollManager][addHandler]配置事件[{}]长连接处理器", notifyEvent);
    }

    private LongPollManager() {
        // private
    }

}
```

#### 5. 请求处理器

定义处理接口

```java
/**
 * @description: 长轮询处理接口
 * @author: panhongtong
 * @date: 2022/8/24 15:56
 **/
public interface LongPollProcessor {

    /**
     * 处理请求
     *
     * @param <T>  数据泛型
     * @param req  req
     * @param data 接收到的数据
     */
    <T> void process(BasicLongPollReq req, T data);

}
```

此处采用模板方法模式，规范处理逻辑

```java
import org.giants.core.bean.R;

import javax.annotation.PostConstruct;

/**
 * @description: 抽象长轮询处理器
 * @author: panhongtong
 * @date: 2022/8/17 17:59
 **/
public abstract class AbsLongPollProcessor implements LongPollProcessor {

    /**
     * 处理请求
     *
     * @param <T>  数据泛型
     * @param req  req
     * @param data 接收到的数据
     */
    @Override
    public <T> void process(BasicLongPollReq req, T data) {
        BasicLongPollContext<T> basicContext = createContext(req, data);
        preProcess(basicContext);
        R<?> resp = doProcess(basicContext);
        basicContext.setResp(resp);
        postProcess(basicContext);
    }

    /**
     * 创建context
     *
     * @param req  请求
     * @param data 接收到的数据
     * @param <T>  数据泛型
     * @return context
     */
    protected abstract <T> BasicLongPollContext<T> createContext(BasicLongPollReq req, T data);

    /**
     * 前置处理
     *
     * @param basicContext 上下文
     * @param <T>          数据泛型
     */
    protected <T> void preProcess(BasicLongPollContext<T> basicContext) {
        // default
    }

    /**
     * 处理逻辑
     *
     * @param <T>          数据泛型
     * @param basicContext 上下文
     * @return 响应结果
     */
    protected abstract <T> R<?> doProcess(BasicLongPollContext<T> basicContext);

    /**
     * 后置处理
     *
     * @param basicContext 上下文
     * @param <T>          数据泛型
     */
    protected <T> void postProcess(BasicLongPollContext<T> basicContext) {
        // default
    }

    /**
     * 获取处理的事件
     *
     * @return 事件
     */
    protected abstract String getNotifyEvent();

    /**
     * 初始化
     * <p> 将处理器加到管理器中
     */
    @PostConstruct
    public void init() {
        LongPollManager.addHandler(getNotifyEvent(), this);
    }

}
```

context用于存储处理方法中所需的数据，用于数据传递

```java
import lombok.Data;
import org.giants.core.bean.R;

import java.util.concurrent.TimeUnit;

/**
 * @description: 基础长轮询处理上下文
 * @author: panhongtong
 * @date: 2022/8/24 17:17
 **/
@Data
public class BasicLongPollContext<T> {

    /**
     * 请求ID
     */
    private String reqId;

    /**
     * 超时时间(默认问29000)
     */
    private Long timeout = 29000L;

    /**
     * 超时时间单位(默认为毫秒)
     */
    private TimeUnit timeoutUint = TimeUnit.MILLISECONDS;

    /**
     * 业务字段
     */
    private String businessKey;

    /**
     * 感兴趣的事件类型
     */
    private String notifyEvent;

    /**
     * 接收到的数据
     */
    private T data;

    /**
     * 响应结果
     */
    private R<?> resp;
}
```

```java
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.giants.web.longpoll.BasicLongPollContext;

import javax.servlet.AsyncContext;

/**
 * @description: Servlet 长轮询处理上下文
 * @author: panhongtong
 * @date: 2022/8/24 17:19
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ServletLongPollContext<T> extends BasicLongPollContext<T> {

    /**
     * 异步上下文
     */
    private AsyncContext asyncContext;

}
```

处理器具体实现

```java
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.giants.core.bean.R;
import org.giants.core.constant.enums.WebStatus;
import org.giants.core.exception.GiantsException;
import org.giants.web.longpoll.AbsLongPollProcessor;
import org.giants.web.longpoll.BasicLongPollContext;
import org.giants.web.longpoll.BasicLongPollReq;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @description: 默认Servlet长轮询请求处理器
 * @author: panhongtong
 * @date: 2022/8/17 18:15
 **/
@Component
public class DefaultServletLongPollProcessor extends AbsLongPollProcessor {

    /**
     * 感兴趣类型
     */
    public static final String NOTIFY_EVENT = "DEFAULT_SERVLET";

    /**
     * 空数据
     */
    private static final R<Object> EMPTY_DATA = R.ok();

    /**
     * 创建context
     *
     * @param req  请求
     * @param data 接收到的数据
     * @param <T>  数据泛型
     * @return context
     */
    @Override
    protected <T> BasicLongPollContext<T> createContext(BasicLongPollReq req, T data) {
        ServletLongPollContext<T> context = new ServletLongPollContext<>();
        BeanUtil.copyProperties(req, context, true);
        context.setData(data);
        return context;
    }

    /**
     * 处理逻辑
     *
     * @param <T>          数据泛型
     * @param basicContext 上下文
     * @return 响应结果
     */
    @Override
    protected <T> R<?> doProcess(BasicLongPollContext<T> basicContext) {
        ServletLongPollContext<T> context = (ServletLongPollContext<T>) basicContext;
        T data = context.getData();
        R<?> respData = Objects.isNull(data) ? EMPTY_DATA : R.ok(data);
        AsyncContext asyncContext = context.getAsyncContext();
        try {
            ServletResponse response = asyncContext.getResponse();
          	// 返回json格式数据
            response.setContentType("application/json");
            response.setCharacterEncoding(asyncContext.getRequest().getCharacterEncoding());
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(respData));
            writer.flush();
        } catch (IOException e) {
            throw new GiantsException(WebStatus.FAIL, e);
        }
        asyncContext.complete();
        return respData;
    }

    /**
     * 获取处理的事件
     *
     * @return 事件
     */
    @Override
    protected String getNotifyEvent() {
        return NOTIFY_EVENT;
    }

}
```

#### 6. 触发事件请求体

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 触发长轮询响应对象
 * @author: panhongtong
 * @date: 2022/8/19 16:49
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongPollTrigger<T> implements Serializable {

    /**
     * 事件
     */
    @NotBlank(message = "事件 不能为空")
    private String notifyEvent;

    /**
     * 数据
     */
    private T data;

}
```

### 3. 测试

1. 启动服务
2. 请求获取数据接口(根据自己情况改端口)

```
http://localhost:39998/longPoll/deferredResult/get
post
{
    "timeout": 30000,
    "notifyEvent": "DEFAULT_SERVLET"
}
```

请求会转圈圈，一直等待服务端响应

若此时一直没有事件触发，就会返回超时结果：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "name": "大福",
        "age": 3
    }
}
```

3. 请求触发事件接口

```
http://localhost:39998/longPoll/trigger
post
{
    "notifyEvent": "DEFAULT_SERVLET",
    "data": {
        "name":"大福",
        "age":3
    }
}
```

请求后观察获取数据接口，已经正常接收到响应数据

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "name": "大福",
        "age": 3
    }
}
```

## 三、DeferredResult实现长轮询

TODO