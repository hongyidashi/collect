# Spring 使用技巧



目录

+ [获取spring容器对象](#获取spring容器对象)
+ [初始化bean](#初始化bean)
+ [自定义类型转换](#自定义类型转换)



## 获取spring容器对象

### 1. 实现BeanFactoryAware接口的方法

```java
@Service
public class PersonService implements BeanFactoryAware {
    private BeanFactory beanFactory;
 
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
 
    public void add() {
        Person person = (Person) beanFactory.getBean("person");
    }
}
```

实现BeanFactoryAware接口，然后重写setBeanFactory方法，就能从该方法中获取到spring容器对象。



### 2. 实现ApplicationContextAware接口

```java
@Service
public class PersonService2 implements ApplicationContextAware {
    private ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
 
    public void add() {
        Person person = (Person) applicationContext.getBean("person");
    }
}
```

实现ApplicationContextAware接口，然后重写setApplicationContext方法，也能从该方法中获取到spring容器对象。



### 3. 实现ApplicationListener接口

```
@Service
public class PersonService3 implements ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;
 
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
    }
 
    public void add() {
        Person person = (Person) applicationContext.getBean("person");
    }
 
}
```

实现ApplicationListener接口，需要注意的是该接口接收的泛型是ContextRefreshedEvent类，然后重写onApplicationEvent方法，也能从该方法中获取到spring容器对象。



关于`Aware`接口，它其实是一个空接口，里面不包含任何方法，它表示已感知的意思，通过这类接口可以获取指定对象，比如：

- 通过BeanFactoryAware获取BeanFactory；
- 通过ApplicationContextAware获取ApplicationContext；
- 通过BeanNameAware获取BeanName等。



## 初始化bean

spring中支持3种初始化bean的方法：

- xml中指定`init-method`方法
- 使用`@PostConstruct`注解
- 实现`InitializingBean`接口



### 1. 使用@PostConstruct注解

```java

```

在需要初始化的方法上增加`@PostConstruct`注解，这样就有初始化的能力。`@PostConstruct`注解其实是Java提供的，该注解被用来修饰一个**非静态的`void()`**方法。被`@PostConstruct`修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，`init()`方法之前执行。

该注解的方法在整个Bean初始化中的执行顺序：

Constructor(构造方法) --> @Autowired(依赖注入) --> @PostConstruct(注释的方法)



### 2. 实现InitializingBean接口

```java

```

实现`InitializingBean`接口，重写`afterPropertiesSet`方法，该方法中可以完成初始化功能。



> init-method、PostConstruct 和 InitializingBean 的执行顺序：PostConstruct --> InitializingBean --> init-method



## 自定义类型转换

spring目前支持3中类型转换器：

1. Converter<S,T>：将 S 类型对象转为 T 类型对象；
2. ConverterFactory<S, R>：将 S 类型对象转为 R 类型及子类对象；
3. GenericConverter：它支持多个source和目标类型的转化，同时还提供了source和目标类型的上下文，这个上下文能让你实现基于属性上的注解或信息来进行类型转换。
   

以Converter为例

### 1. 实现接口

```java

```

实现 Converter<S,T> 接口，重写 convert(S) 接口方法。



### 2. **注册类型转换器**

```java

```

将新定义的类型转换器注入到spring容器中，在`WebMvcConfigurerAdapter` 中的`addFormatters(FormatterRegistry registry)`中添加。



