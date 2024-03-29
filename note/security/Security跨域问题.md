# Security跨域问题

目录

+ [跨域失效](#跨域失效)
+ [API网关和跨域](#API网关和跨域)

## <span id="跨域失效">跨域失效</span>

**具体问题**：加了 Spring Security 后 Spring Boot 中的跨域失效

**解决方案**：

```
@Override
public void configure(HttpSecurity http) throws Exception {
    // 加入下面这段代码即可
    http.cors();
    ...
}
```

**问题成因**：  
我们解决跨域问题一般采用如下方法：

```
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowCredentials(true)
            .allowedMethods("*");
}

@Bean
public CorsFilter corsFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(source);
}
```

而其本质都是创建一个 CorsRegistry 放到上下文中，然后会在 Interceptor 或者 Handler 层进行 CORS 验证。

> 首先了解一个概念：**Preflight request**  
> 本来post是一个“简单请求”但是因为我需要带authorization的header，所以就变成了“复杂请求”，而复杂请求就需要先发送options请求来询问你的服务器，允不允许你发送本次请求。而这次options请求就叫 Preflight request。

然后：

1. preflight request 不会携带认证信息；
2. Spring Security 通过 Filter 来进行身份验证；
3. Interceptor 和 HttpRequestHanlder 在 DispatcherServlet 之后被调用；
4. Spring Security 中的 Filter 优先级比我们注入的 CorsFilter 优先级高。

总结一下就是 **Preflight request 无法通过身份验证，从而导致 CORS 失效**。
> 参考：https://mp.weixin.qq.com/s/2zKPj3tKUzKmR6PhSEUzJA

## API网关和跨域

**问题现状**  
首先发现问题的原始点是浏览器进行 ajax 请求的时候出现跨域问题，经查看请求报文和错误确定是 Access-Control-Allow-Origin 出现了多个值（浏览器目前是不允许的），其原因是是在 gateway 中配置过了
Access-Control-Allow-Origin，后端服务的开发人员也配置了 Access-Control-Allow-Origin，导致 response 在响应的时候 Access-Control-Allow-Origin
出现了多个值（不管两个值相同还是不同浏览器目前都会报错），截图如下：
![png](images/问题截图.png)

**解决该问题的思路**

- 可以将所有后端服务的跨域处理都去除，交网关统一处理
- 可以将网关的处理去除（那么后端所有服务都需要添加）
- 在网关做去重处理，只保留一个值响应给浏览器（这是本文选择的处理方法）

这个问题是 gateway 本身的bug，预计后续版本可能会修复  
我目前采用的版本为：**Spring Cloud Gateway : 2.2.5.RELEASE**