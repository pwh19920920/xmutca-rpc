package com.xmutca.rpc.consumer;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.consumer.Reference;
import com.xmutca.rpc.core.rpc.exchange.ClientExchange;
import com.xmutca.rpc.example.api.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
@Component
public class NettyRunner implements CommandLineRunner {
//
    @Reference(serviceName = "test", group = "order", interfaceClass = HelloService.class)
    private HelloService helloService;

    @Autowired
    private TestService testService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(helloService.sayHello(testService.test()));
    }
}
