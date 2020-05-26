# Xmutca-rpc

Xmutca-rpc是一个基于netty开发的分布式服务框架，提供稳定高性能的RPC远程服务调用功能，支持注册中心，服务治理，负载均衡等特性，开箱即用。

## 模块介绍
- 1.xmutca-rpc-core             rpc核心模块
- 2.xmutca-rpc-example          rpc示例
- 3.xmutca-rpc-local            本地容器实现
- 4.xmutca-rpc-registry-nacos   nacos注册中心
- 5.xmutca-rpc-spring           spring容器实现

## 实现功能
- 1.核心实现：SPI扩展，支持动态加载拓展实现，具体可以看实现类ExtensionLoader
- 2.负载均衡：现在已支持轮训负载，随机负载，也可自定义
- 3.集群容错：FailFast快速失败、 FailOver故障切换，FailSafe故障安全
- 4.注册发现：目前支持接入nacos
- 5.容器支持：支持本地java容器，支持spring容器
- 6.注解拓展：支持注解加载提供者，注解获取远程rpc对象
- 7.极速拓展：允许加载自定义的过滤器，自定义容器，自定义注册中心等等。

## 快速开始
```
1. maven配置
<dependency>
    <groupId>com.xmutca</groupId>
    <artifactId>xmutca-rpc-core</artifactId>
    <version>0.0.1-SNAPSHOT </version>
</dependency>

<dependency>
    <groupId>com.xmutca</groupId>
    <artifactId>xmutca-rpc-spring</artifactId>
    <version>0.0.1-SNAPSHOT </version>
</dependency>

<dependency>
    <groupId>com.xmutca</groupId>
    <artifactId>xmutca-rpc-registry-nacos</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

2. 定义接口
public interface HelloService {

    /**
     * test
     * @param msg
     * @return
     */
    String sayHello(String msg);
}

3. 提供者实现接口
@Component
@Provider(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Autowired
    private TestService testService;

    @Override
    public String sayHello(String msg) {
        return testService.test() + " -> " + msg;
    }
}

4. 提供者配置
rpc:
  registry: nacos://localhost:8848 # 注册中心地址
  provider: # 提供者配置
    corePoolSize: 60       # 核心线程数
    maxPoolSize: 300       # 最大线程数
    port: 8886             # 提供者端口
    scanPackage: com.xmutca.rpc.provider.facade   # 扫描提供者包路径
    metadata:              # 服务元数据
      group: "order"       # 服务分组
      serviceName: "test"  # 服务名称
      version: "v1.0.0"    # 服务版本


5. 启动提供者
@EnableXmutcaRpc
@SpringBootApplication
public class XmutcaSpringProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmutcaSpringProviderApplication.class, args);
    }
}

6. 消费者引用
@RestController
@RequestMapping
public class ControllerTest {

    @Reference(serviceName = "test", group = "order", interfaceClass = HelloService.class)
    private HelloService helloService;

    @RequestMapping("/test")
    public Object test() {
        return helloService.sayHello("test");
    }
}

7. 消费者配置
rpc:
  registry: nacos://localhost:8848
  consumer:
    - timeout: 1000               # 超时配置
      metadata:
        group: "order"            # 服务分组
        serviceName: "test"       # 服务名称
        version: "v1.0.0"         # 服务版本

8. 启动消费者
@EnableXmutcaRpc
@SpringBootApplication
public class XmutcaSpringConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmutcaSpringConsumerApplication.class, args);
    }
}
```

## 作者题外话

此项目欢迎各位gay友试用，互相学习，及时反馈，谢谢。