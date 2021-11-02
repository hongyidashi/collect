# Spring-AOP

<!-- START doctoc generated TOC please keep comment here to allow auto update -->

- [一、Spring AOP概述](#%E4%B8%80spring-aop%E6%A6%82%E8%BF%B0)
- [二、Spring AOP一代](#%E4%BA%8Cspring-aop%E4%B8%80%E4%BB%A3)
    - [1. Pointcut](#1--pointcut)
    - [2. Jointpoint](#2-jointpoint)
    - [3. Advice](#3-advice)
    - [4. Advisor](#4-advisor)
    - [5. 织入](#5-%E7%BB%87%E5%85%A5)
    - [6. Spring AOP的自动动态代理](#6-spring-aop%E7%9A%84%E8%87%AA%E5%8A%A8%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
- [三、Spring AOP自动动态代理的实现方式](#%E4%B8%89spring-aop%E8%87%AA%E5%8A%A8%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86%E7%9A%84%E5%AE%9E%E7%8E%B0%E6%96%B9%E5%BC%8F)
- [四、 Spring AOP二代（集成了AspectJ）](#%E5%9B%9B-spring-aop%E4%BA%8C%E4%BB%A3%E9%9B%86%E6%88%90%E4%BA%86aspectj)
    - [1. EnableAspectJAutoProxy有啥用？](#1-enableaspectjautoproxy%E6%9C%89%E5%95%A5%E7%94%A8)
    - [2. 切点表达式](#2-%E5%88%87%E7%82%B9%E8%A1%A8%E8%BE%BE%E5%BC%8F)
- [五、Adivce之间的顺序关系](#%E4%BA%94adivce%E4%B9%8B%E9%97%B4%E7%9A%84%E9%A1%BA%E5%BA%8F%E5%85%B3%E7%B3%BB)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 一、Spring AOP概述

我们在使用Spring框架的时候，经常需要和Spring的2大特性，IOC和AOP打交道，本篇文章就分享一下AOP的底层实现，比较基础的内容本篇文章就不多做介绍了，主要侧重于底层api的设计理念。

**「AOP这种设计理念常见的概念如下」**

![png](images/spring-aop-AOP设计理念常见概念.png)

**「AOP的主要应用场景如下」**

![png](images/spring-aop-AOP主要应用场景.png)

**「Spring AOP的实现主要经历了2代」**

第一代：spring1.x版本，自己实现了AOP的功能

第二代：spring2.x版本，Spring集成了AspectJ的实现

## 二、Spring AOP一代

**「当我们要基于现成的实现增加横切逻辑时，首先需要找到哪些地方增强，我们就用Pointcut来进行筛选吧」**

先写一个Service方便后面的演示

```java
public interface EchoService {
    String echo(String message);
}

public class DefaultEchoService implements EchoService {
    @Override
    public String echo(String message) {
        return message;
    }
}
```

### 1. Pointcut

Pointcut接口定义如下

```java
public interface Pointcut {

    // 通过类过滤
    ClassFilter getClassFilter();

    // 通过方法过滤
    MethodMatcher getMethodMatcher();

    Pointcut TRUE = TruePointcut.INSTANCE;

}
```

**「当我们想筛选出EchoService的echo方法时，就可以定义如下的Pointcut」**

```java
public class EchoPointcut implements Pointcut {

    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
                return EchoService.class.isAssignableFrom(clazz);
            }
        };
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return "echo".equals(method.getName()) &&
                        method.getParameterTypes().length == 1 &&
                        Objects.equals(String.class, method.getParameterTypes()[0]);
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                return false;
            }
        };
    }
}
```

看起来还是很麻烦的，因此Spring内置了很多实现，一般情况下我们用内置的实现即可，不用自己定义，上面的筛选过程就可以改为如下

```java
// 方法名为 echo 会被拦截
NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut();
        pointcut.setMappedName("echo");
```

Spring提供的部分Pointcut实现如下：

![png](images/spring-aop-Spring提供的部分Pointcut实现.png)

### 2. Jointpoint

**「通过Pointcut筛选出来的要增加横切逻辑的地方就是Jointpoint。」** 在AOP理念中，很多地方可以增加横切逻辑，如方法执行，字段设置等。但是**「Spring只支持方法执行这一种Joinpoint」**
，因为这种类型的Jointpoint基本上已经满足80%的场景了

Joinpoint类型中 **「方法调用优于方法执行」**

![png](images/spring-aop-Joinpoint类型.png)

因为Spring中只支持方法执行这一种Joinpoint，所以我们可以从Joinpoint实现类中获取增强的方法信息

![png](images/spring-aop-Joinpoint实现类.png)

### 3. Advice

当筛选出Jointpoint时，我们就需要在这些Jointpoint上增加横切逻辑，这些横切逻辑被称为Advice

![png](images/spring-aop-AOP增强操作.png)

在Spring中实现横切逻辑的方式有两类：

1. 实现Advice接口
2. 实现IntroductionInfo接口

实现Advice接口的方式我们最常用，后面会详细分析。实现IntroductionInfo接口的方式基本不会用，这里演示一下具体的用法，方便理解整个AOP API的设计理念

**「IntroductionInfo主要是通过给目标类实现特定接口来增加新功能」**

```java
public interface SayName {
    String getName();
}

public class DefaultSayName implements SayName {
    @Override
    public String getName() {
        return "I am service";
    }
}

    public static void main(String[] args) {
        SayName sayName = new DefaultSayName();
        EchoService echoService = new DefaultEchoService();
        // IntroductionInfo接口的内置实现
        DelegatingIntroductionInterceptor interceptor =
                new DelegatingIntroductionInterceptor(sayName);
        Advisor advisor = new DefaultIntroductionAdvisor(interceptor, SayName.class);
        ProxyFactory proxyFactory = new ProxyFactory(echoService);
        proxyFactory.addAdvisor(advisor);
        // hello world
        EchoService proxyService = (EchoService) proxyFactory.getProxy();
        System.out.println(proxyService.echo("hello world"));
        // I am service
        SayName proxySayName = (SayName) proxyFactory.getProxy();
        System.out.println(proxySayName.getName());
    }
```

可能你对这个例子中的Advisor和ProxyFactory比较陌生，不知道起了啥作用，不着急，我们后面会详细分析这2个类的作用

**「实现Advice接口的方式，应该是Spring AOP一代中最常见的使用方式了」**

**「对HashMap的put方法增加执行前的横切逻辑」**, 打印放入HashMap的key和value的值

```java
public static void main(String[]args){
        JdkRegexpMethodPointcut pointcut=new JdkRegexpMethodPointcut();
        pointcut.setPattern(".*put.*");
        DefaultPointcutAdvisor advisor=new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new MethodBeforeAdvice(){
@Override
public void before(Method method,Object[]args,Object target)throws Throwable{
        System.out.printf("当前存放的key为 %s，值为 %s",args[0],args[1]);
        }
        });

        ProxyFactory proxyFactory=new ProxyFactory(new HashMap());
        proxyFactory.addAdvisor(advisor);
        Map<String, String> proxyMap=(Map<String, String>)proxyFactory.getProxy();
        // 当前存放的key为 a，值为 a
        proxyMap.put("a","a");
        }
```

### 4. Advisor

前面我们说过在AOP设计理念中，我们用Aspect来声明切面，每个Aspect可以包含多个Pointcut和Advice。

**「在Spring AOP一代中，Aspect对应的实现为Advisor」**。即Advisor是Pointcut和Advice的容器，但是一个Advisor只能包含一个Pointcut和Advice

![png](images/spring-aop-Advisor分类.png)

因为Advice的实现方式有两类，因此对应的Advisor也可以分为两类

### 5. 织入

**「在Spring中将Advice织入到Jointpoint的过程是通过动态代理来实现的」**。当然织入的方式有很多种，不仅仅只有动态代理这一种实现

![png](images/spring-aop-织入分类.png)

Spring用了jdk动态代理和cglib来实现动态代理。生成代理对象用了工厂模式。从api中就可以很清晰的看出来

![png](images/spring-aop-动态代理实现.png)

**「jdk动态代理」**

```java
public class CostInvocationHandler implements InvocationHandler {

    private Object target;

    public CostInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("cost " + cost);
        return result;
    }
}

    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader,
                new Class[]{EchoService.class},
                new CostInvocationHandler(new DefaultEchoService()));
        EchoService echoService = (EchoService) proxy;
        // cost 0
        // hello world
        System.out.println(echoService.echo("hello world"));
    }
```

**「cglib」**

```java
public static void main(String[]args){
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(DefaultEchoService.class);
        enhancer.setInterfaces(new Class[]{EchoService.class});
        enhancer.setCallback(new MethodInterceptor(){
@Override
public Object intercept(Object source,Method method,Object[]args,MethodProxy methodProxy)throws Throwable{
        long startTime=System.currentTimeMillis();
        Object result=methodProxy.invokeSuper(source,args);
        long cost=System.currentTimeMillis()-startTime;
        System.out.println("cost "+cost);
        return result;
        }
        });
        EchoService echoService=(EchoService)enhancer.create();
        // cost 29
        // hello world
        System.out.println(echoService.echo("hello world"));
        }
```

### 6. Spring AOP的自动动态代理

上面我们一直通过API的形式来演示，我们当然也可以把这些对象放入Spring容器，让Spring来管理，并且对Spring容器中的Bean生成代理对象

上面的Demo可以改为如下形式，变化基本不大

**「手动配置」**

```java
public class ProxyConfig {

    // 创建代理对象
    @Bean
    public EchoService echoService() {
        return new DefaultEchoService();
    }

    // 创建advice
    @Bean
    public CostMethodInterceptor costInterceptor() {
        return new CostMethodInterceptor();
    }

    // 使用pointcut和advice创建advisor
    @Bean
    public Advisor advisor() {
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName("echo");
        advisor.setAdvice(costInterceptor());
        return advisor;
    }

    // 创建代理对象
    @Bean("echoProxy")
    public ProxyFactoryBean proxyFactoryBean(EchoService echoService) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(echoService);
        proxyFactoryBean.setInterceptorNames("advisor");
        return proxyFactoryBean;
    }
}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProxyConfig.class);
        // 获取代理对象
        EchoService echoService = (EchoService) context.getBean("echoProxy");
        // cost 0
        // hello world
        System.out.println(echoService.echo("hello world"));
    }
```

**「可以看到我们对每个生成的代理对象都要配置对应的ProxyFactoryBean，然后从容器中获取代理对象来使用」**。当代理对象很少时还能应付，当代理对象很多时，那还不得累到吐血。有没有什么简单的办法呢？

Spring肯定也想到了这个问题，所以他提供了如下一个类DefaultAdvisorAutoProxyCreator来实现自动代理，我们将这个类放入Spring容器即可，如下所示

**「自动配置」**

```java
public class AutoProxyConfig {

    // 创建代理对象
    @Bean
    public EchoService echoService() {
        return new DefaultEchoService();
    }

    // 创建advice
    @Bean
    public CostMethodInterceptor costInterceptor() {
        return new CostMethodInterceptor();
    }

    // 使用pointcut和advice创建advisor
    @Bean
    public Advisor advisor() {
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName("echo");
        advisor.setAdvice(costInterceptor());
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AutoProxyConfig.class);
        EchoService echoService = context.getBean(EchoService.class);
        // cost 0
        // hello world
        System.out.println(echoService.echo("hello world"));
    }
```

从容器中获取的对象直接就是被代理后的对象，非常方便。**「Spring
AOP提供了很多类来实现自动代理，但他们有一个共同的父类AbstractAutoProxyCreator，看来自动代理的秘密就在这个AbstractAutoProxyCreator类中」**

![png](images/spring-aop-AOP代理对象创建.png)

## 三、Spring AOP自动动态代理的实现方式

如果让你实现对象的自动代理，你会怎么做呢？

当然是通过BeanPostProcessor来干预Bean的声明周期，聪明！Spring就是这么干的，来验证一下我们的想法

![png](images/spring-aop-BeanPostProcessor类继承关系.png)

看这个类的继承关系，基本上就验证了我们的想法了。我们只要看看他重写了BeanPostProcessor的哪些方法即可？

**「AbstractAutoProxyCreator重写了如下2个重要的方法」**

1. postProcessBeforeInstantiation（Bean实例化前阶段执行）
2. postProcessAfterInitialization（Bean初始化后阶段执行）

![png](images/spring-aop-bean实例化过程.png)

**「postProcessBeforeInstantiation（Bean实例化前阶段执行）」**

![png](images/spring-aop-postProcessBeforeInstantiation.png)

当用户自定义了TargetSource的实现时，会从TargetSource中获取目标对象生成代理。但是一般情况下我们很少会自定义TargetSource的实现。所以这部分就不再分析了。直接看postProcessAfterInitialization

**「postProcessAfterInitialization（Bean初始化后阶段执行）」**

![png](images/spring-aop-postProcessAfterInitialization.png)

如果没有经过代理的化就会进入wrapIfNecessary方法

![png](images/spring-aop-wrapIfNecessary.png)

思路很简单，就是根据Bean获取对应的Advisor，然后创建其代理对象，并返回。

![png](images/spring-aop-AOP和IOC.png)

**「所以当面试官问你Spring AOP和IOC是如何结合在一起的时候，你是不是知道该如何回答了？」**

在Bean生命周期的Bean初始化后阶段，如果这个Bean需要增加横切逻辑，则会在这个阶段生成对应的代理对象。

## 四、 Spring AOP二代（集成了AspectJ）

当Spring 2.0发布以后，Spring AOP增加了新的使用方式，Spring AOP集成了AspectJ。我们最常用的就是这个版本的Spring AOP

主要有如下变化

1. 可以用POJO来定义Aspect和Adivce，并提供了一系列相应的注解，如@Aspect和@Around等。而不用像1.x版本中实现相应的接口
2. 支持aspectj中的pointcut的表达方式，我们都深有体会哈

![png](images/spring-aop-Adivce注解.png)

演示一下2.0版本中aop的使用方式

定义切面

```java

@Aspect
public class AspectDefine {

    @Pointcut("execution(* com.collect.proxy.EchoService.echo(..))")
    public void pointcutName() {
    }

    @Around("pointcutName()")
    public Object calCost(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("cost " + cost);
        return result;
    }

    @Before("pointcutName()")
    public void beforeMethod() {
        System.out.println("beforeMethod");
    }
}
```

增加配置，注入实现类

```java

@EnableAspectJAutoProxy
public class AspectJConfig {
    @Bean
    public EchoService echoService() {
        return new DefaultEchoService();
    }
}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AspectJConfig.class, AspectDefine.class);
        EchoService echoService = context.getBean(EchoService.class);
        // beforeMethod
        // cost 0
        // hello world
        System.out.println(echoService.echo("hello world"));
        context.close();
    }
```

**「虽然spring2.0之后spring aop集成了AspectJ，但实际上只是拿AspectJ的“皮大衣“用了一下，因为底层的实现和织入方式还是1.x原先的实现体系」**

### 1. EnableAspectJAutoProxy有啥用？

**「当我们想使用2.0版本的aop时，必须在配置类上加上@EnableAspectJAutoProxy注解，那么这个注解有啥作用呢？」**

```java

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {

    boolean proxyTargetClass() default false;

    boolean exposeProxy() default false;

}
```

可以看到很重要的一句

```
@Import(AspectJAutoProxyRegistrar.class)
```

通过@Import注入bean，**「通过@Import注解注入Bean的方式有如下三种」**

1. 基于Configuration Class
2. 基于ImportSelector接口
3. 基于ImportBeanDefinitionRegistrar接口

![png](images/spring-aop-AspectJAutoProxyRegistrar.png)

这个代码主要做了2个事情

1. 往容器中注入AnnotationAwareAspectJAutoProxyCreator
2.
当@EnableAspectJAutoProxy注解中的proxyTargetClass或者exposeProxy属性为true的时候，将AnnotationAwareAspectJAutoProxyCreator中的proxyTargetClass或者exposeProxy属性改为true

**「proxyTargetClass和exposeProxy保存在AnnotationAwareAspectJAutoProxyCreator类的父类ProxyConfig中，这个类存了一些配置，用来控制代理对象的生成过程」**

proxyTargetClass：true使用CGLIB基于类创建代理；false使用java接口创建代理

exposeProxy：true将代理对象保存在AopContext中，否则不保存

第一个属性比较容易理解，那么第二个属性有啥作用呢？演示一下

```java

@Service
public class SaveSevice {

    public void method1() {
        System.out.println("method1 executed");
        method2();
    }

    public void method2() {
        System.out.println("method2 executed");
    }
}

@Aspect
public class AspectDefine {

    @Pointcut("execution(* com.collect.invalid.SaveSevice.method2(..))")
    public void pointcutName() {
    }

    @Around("pointcutName()")
    public Object calCost(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("开启事务");
        return joinPoint.proceed();
    }
}

@EnableAspectJAutoProxy
public class InvalidDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SaveSevice.class,
                        AspectDefine.class, InvalidDemo.class);
        SaveSevice saveSevice = context.getBean(SaveSevice.class);
        saveSevice.method1();
        System.out.println("--");
        saveSevice.method2();
    }
}
```

结果为

```
method1 executed
method2 executed
--
开启事务
method2 executed
```

**「可以看到通过method1调用method2时，aop没有生效。直接调用method2时，aop才会生效。事务方法自调用失效就是因为这个原因，因为调用的不是代理对象的方法」**

解决方法有很多种，例如重新从ApplicationContext中取一下代理对象，然后调用代理对象的方法。另一种就是通过AopContext获取代理对象，实现原理就是当方法调用时会将代理对象放到ThreadLocal中

```
@Service
public class SaveSevice {

    public void method1() {
        System.out.println("method1 executed");
        ((SaveSevice) AopContext.currentProxy()).method2();
    }

    public void method2() {
        System.out.println("method2 executed");
    }
}
```

将exposeProxy属性改为true

```
@EnableAspectJAutoProxy(exposeProxy = true)
method1 executed
开启事务
method2 executed
--
开启事务
method2 executed
```

可以看到aop成功生效。**「当你使用@Transactional注解，分布式事务框架时一定要注意子调用这个问题，不然很容易造成事务失效」**

我们接着聊，往容器中注入AnnotationAwareAspectJAutoProxyCreator，那么这个类有啥作用呢？

![png](images/spring-aop-AnnotationAwareAspectJAutoProxyCreator继承关系.png)

看这继承关系是不是和我们上面分析的DefaultAdvisorAutoProxyCreator类很相似，这不就是为了开启自动代理吗？

忘了自动代理的实现过程了？回头看看

### 2. 切点表达式

**「Spring AOP用AspectJExpressionPointcut桥接了Aspect的筛选能力」**。其实Aspect有很多种类型的切点表达式，但是Spring
AOP只支持如下10种，因为Aspect支持很多种类型的JoinPoint，但是Spring AOP只支持方法执行这一种JoinPoint，所以其余的表达式就没有必要了。

![png](images/spring-aop-切点类型.png)

因为AspectJ提供的表达式在我们工作中经常被使用，结合Demo演示一下具体的用法

| 表达式类型  | 解释                                       |
| :---------- | :----------------------------------------- |
| execution   | 匹配方法表达式，首选方式                   |
| within      | 限定类型                                   |
| this        | 代理对象是指定类型 ，所有方法都会被拦截    |
| target      | 目标对象是指定类型，所有方法都会被拦截     |
| args        | 匹配方法中的参数                           |
| @target     | 目标对象有指定的注解，所有方法都会被拦截   |
| @args       | 方法参数所属类型上有指定注解               |
| @within     | 调用对象上有指定的注解，所有方法都会被拦截 |
| @annotation | 有指定注解的方法                           |

**「execution」**

匹配方法表达式，首选方式

```
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
                throws-pattern?)
```

拦截Performance类的perform方法的切点表达式如下

![png](images/spring-aop-execution表达式.png)

放几个官方的Demo

```
// The execution of any public method:
execution(public * *(..))

// The execution of any method with a name that begins with set
execution(* set*(..))

// The execution of any method defined by the AccountService interface
execution(* com.xyz.service.AccountService.*(..))

// The execution of any method defined in the service package:
execution(* com.xyz.service.*.*(..))
```

**「within」**限定类型

```
// 拦截service包中任意类的任意方法
within(com.xyz.service.*)

// 拦截service包及子包中任意类的任意方法
within(com.xyz.service..*)
```

**「this」**

代理对象是指定类型，所有方法都会被拦截

举个例子说明一下

```java

@Configuration
@EnableAspectJAutoProxy
public class ThisDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ThisDemo.class, AspectDefine.class);
        Name name = context.getBean(Name.class);
        name.getName();
        System.out.println(name instanceof Student);
    }


    @Aspect
    public class AspectDefine {
        @Before("this(com.collect.aspectjPointcut.thisDemo.ThisDemo.Student)")
        public void before() {
            System.out.println("before");
        }
    }

    @Bean
    public Student student() {
        return new Student();
    }

    public class Student implements Name {

        @Override
        public String getName() {
            return null;
        }
    }

    public interface Name {
        String getName();
    }
}
```

输出为

```
false
```

有接口时会使用jdk动态代理，因此代理对象为Proxy，不会拦截

当设置为jdk动态代理为，代理对象为Student，正常拦截

将注解改为如下形式 @EnableAspectJAutoProxy(proxyTargetClass = true)

输出为

```
before
true
```

**「target」**目标对象是指定类型，所有方法都会被拦截

```
// 目标对象为AccountService类型的会被代理
target(com.xyz.service.AccountService)
```

this 和 target 的不同点**「this作用于代理对象，target作用于目标对象」**

**「args」**匹配方法中的参数

```
// 匹配只有一个参数，且类型为com.ms.aop.args.demo1.UserModel
@Pointcut("args(com.ms.aop.args.demo1.UserModel)")

// 匹配多个参数
args(type1,type2,typeN)

// 匹配第一个参数类型为com.ms.aop.args.demo1.UserModel的所有方法, .. 表示任意个参数
@Pointcut("args(com.ms.aop.args.demo1.UserModel,..)")
```

**「@target」**目标对象有指定的注解，所有方法都会被拦截

```
// 目标对象中包含com.ms.aop.jtarget.Annotation1注解，调用该目标对象的任意方法都会被拦截
@target(com.ms.aop.jtarget.Annotation1)
```

**「@args」**方法参数所属类型上有指定注解

```
// 匹配1个参数，且第1个参数所属的类中有Anno1注解
@args(com.ms.aop.jargs.demo1.Anno1)
// 匹配多个参数，且多个参数所属的类型上都有指定的注解
@args(com.ms.aop.jargs.demo1.Anno1,com.ms.aop.jargs.demo1.Anno2)
// 匹配多个参数，且第一个参数所属的类中有Anno1注解
@args(com.ms.aop.jargs.demo2.Anno1,…)
```

**「@within」**

调用对象上有指定的注解，所有方法都会被拦截

```
// 声明有com.ms.aop.jwithin.Annotation1注解的类中的所有方法都会被拦截
@within(com.ms.aop.jwithin.Annotation1)
```

**「@target 和 @within 的不同点」**@target关注的是被调用的对象，@within关注的是调用的对象

**「@annotation」**有指定注解的方法

```
// 被调用方法上有Annotation1注解
@annotation(com.ms.aop.jannotation.demo2.Annotation1)
```

## 五、Adivce之间的顺序关系

一个方法被一个aspect类拦截时的执行顺序如下

@Around->@Before->方法执行->@Around->@After->@AfterReturning/@AfterThrowing

当方法正常结束时，执行@AfterReturning。方法异常结束时，执行@AfterThrowing。两者不会同时执行哈

![png](images/spring-aop-aspect类拦截时的执行顺序.png)

一个方法被多个aspect类拦截时的执行顺序如下：

![png](images/spring-aop-被多个aspect类拦截时的执行顺序.png)

**「多个aspect的执行顺序可以通过@Order注解或者实现Oreder接口来控制」**

**「Adivce的顺序一定要梳理清楚，不然有时候产生的很多魔幻行为你都不知道怎么发生的」**

