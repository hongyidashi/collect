# Spring的一些常见问题

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [一、SpringBoot启动原理](#%E4%B8%80springboot%E5%90%AF%E5%8A%A8%E5%8E%9F%E7%90%86)
- [二、Spring循环依赖](#%E4%BA%8Cspring%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96)
- [三、@Autowired和@Resource的区别](#%E4%B8%89autowired%E5%92%8Cresource%E7%9A%84%E5%8C%BA%E5%88%AB)
    - [1. @Autowired](#1-autowired)
    - [2. @Resource](#2-resource)
- [四、事务失效](#%E5%9B%9B%E4%BA%8B%E5%8A%A1%E5%A4%B1%E6%95%88)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 一、SpringBoot启动原理

~~不会，滚~~

Springboot启动依靠的是启动类上的注解`@SpringBootApplication`，而它是个组合注解，主要由`@Configuration`、`@EnableAutoConfiguration`及`@ComponentScan`
组成。  
其中`@Configuration`的作用是标识这个类可以使用Spring IoC容器作为bean定义的来源；  
`@ComponentScan`的功能是自动扫描并加载符合条件的组件（比如@Component和@Repository等）或者bean定义，最终将这些bean定义加载到IoC容器中；  
`@EnableAutoConfiguration`是自动装配的核心，可以帮助SpringBoot应用将所有符合条件的@Configuration配置都加载到当前SpringBoot创建并使用的IoC容器。

## 二、Spring循环依赖

~~啊？我没遇到过啊，哪个憨批会写这样的代码，你吗？~~

Spring通过三级缓存的方式解决循环依赖的问题。

- 第一级缓存：用来保存实例化、初始化都完成的对象
- 第二级缓存：用来保存实例化完成，但是未初始化完成的对象
- 第三级缓存：用来保存一个对象工厂，提供一个匿名内部类，用于创建二级缓存中的对象

举个栗子：  
假设一个简单的循环依赖场景，A、B互相依赖；

1. 创建对象A，实例化的时候把A对象工厂放入三级缓存；
2. A注入属性时，发现依赖B，去就依次从一、二、三级缓存中找B，没找着，转而去实例化B；
3. 实例化B的时候发现依赖于A，就又依次从一、二、三级缓存中找A；
4. 在三级缓存中找到了A，删除三级缓存中的A(工厂)，然后A放入二级缓存；此时，并实例化成功B，又将B放入一级缓存；
5. 实例化B后继续实例化A，现在会顺利在一级缓存中找到B的示例，而后，实例化A完成；
6. 差点忘了，实例化完成后还要删掉二级缓存中的A，将A加入到一级缓存中。

> 那为毛要三级缓存？二级不行吗？

呃，如果仅仅只是为了解决循环依赖的问题，是阔以的。  
使用三级缓存是考虑到**注入的对象如果实现了AOP**，那么注入到其他bean的时候，不是最终的代理对象，而是原始的。通过三级缓存的ObjectFactory才能实现类最终的代理对象。

还是举个栗子吧：

0. 假设只有二级缓存的情况，且A实现了AOP
1. 创建对象A，实例化的时候把A对象放入二级缓存；
2. A注入属性时...巴拉巴拉...
3. 与此同时，有个对象C依赖了A，在二级缓存中发现了未实例化完全的A(是个原对象，普通的Bean对象)，美滋滋拿去用了；
4. A搞定了B后，自己也实例化完成并生成了代理对象，将原来的普通A覆盖了；
5. 现在就会发现，C手中的A和现在的A好像不太一样，一般来讲，IOC中的Bean又都是单例的，这就好像就8太对了。

这部分比较麻烦，实在想了解建议自行百度，简单来说就是：  
假设只有二级缓存的情况，往二级缓存中放的显示一个普通的Bean对象，BeanPostProcessor去生成代理对象之后，覆盖掉二级缓存中的普通Bean对象，那么多线程环境下可能取到的对象就不一致了。~~管你听没听懂~~

## 三、@Autowired和@Resource的区别

### 1. @Autowired

1. @Autowired为Spring提供的注解，需要导入包org.springframework.beans.factory.annotation.Autowired；
2. @Autowired采取的策略为按照类型注入，当一个类型有多个bean值的时候，会造成无法选择具体注入哪一个的情况，这个时候我们需要配合着@Qualifier使用。

### 2. @Resource

1. @Resource注解由J2EE提供，需要导入包javax.annotation.Resource；
2. @Resource默认按照ByName自动注入
3. 如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常；
4. 如果指定了name，则从上下文中查找名称（id）匹配的bean进行装配，找不到则抛出异常；
5. 如果指定了type，则从上下文中找到类似匹配的唯一bean进行装配，找不到或是找到多个，都会抛出异常；
6. 如果既没有指定name，又没有指定type，则自动按照byName方式进行装配；如果没有匹配，则回退为一个原始类型进行匹配，如果匹配则自动装配。

## 四、事务失效

**可能原因**：

1. 存储引擎为MyISAM不支持事务  
   **解决方案**：修改存储引擎为Innodb

2. 业务代码层面

- 我们要使用Spring的声明式事务，那么需要执行事务的Bean是否已经交由了Spring管理？在代码中的体现就是类上是否有@Service、Component等一系列注解  
  **解决方案**：将Bean交由Spring进行管理（添加@Service注解）

- `@Transactional`注解是否被放在了合适的位置。默认情况下无法使用`@Transactional`对一个非public的方法进行事务管理  
  **解决方案**：修改需要事务管理的方法为public

- 出现了自调用，如：

```java
// 示例一
@Service
public class DmzService {
    public void saveAB(A a, B b) {
        saveA(a);
        saveB(b);
    }

    @Transactional
    public void saveA(A a) {
        dao.saveA(a);
    }

    @Transactional
    public void saveB(B b) {
        dao.saveB(a);
    }
}
```

或

```java
// 示例二
@Service
public class DmzService {
    @Transactional
    public void save(A a, B b) {
        saveB(b);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveB(B b) {
        dao.saveB(a);
    }
}
```

**问题成因**：**自调用时，调用的是目标类中的方法而不是代理类中的方法**
。Spring中事务的实现是依赖于AOP的，当容器在创建dmzService这个Bean时，发现这个类中存在了被@Transactional标注的方法（修饰符为public）那么就需要为这个类创建一个代理对象并放入到容器中，会对被@Transactional标注的方法进行拦截并嵌入事务管理的逻辑，而案例一种的saveAB方法上没有@Transactional，相当于代理类直接调用了目标类中的方法；在案例二中实际上在调用saveB方法时，是直接调用的目标类中的saveB方法，在saveB方法前后并不会有事务的开启或者提交、回滚等操作。  
**解决方案**：

1. 自己注入自己，然后显示的调用
2. 利用AopContext，如下：

```java

@Service
public class DmzService {
    @Transactional
    public void save(A a, B b) {
        ((DmzService) AopContext.currentProxy()).saveB(b);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveB(B b) {
        dao.saveB(a);
    }
}
```

> 使用该方法时，需要在配置类上新增一个配置：  
> // exposeProxy=true代表将代理类放入到线程上下文中，默认是false  
> @EnableAspectJAutoProxy(exposeProxy = true)
