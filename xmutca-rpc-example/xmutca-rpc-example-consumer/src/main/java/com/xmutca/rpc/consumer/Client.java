package com.xmutca.rpc.consumer;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.consumer.GenericInvoker;
import com.xmutca.rpc.core.consumer.GenericProxyFactory;
import com.xmutca.rpc.core.rpc.exchange.ClientExchange;
import com.xmutca.rpc.example.api.HelloService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/21
 */
public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        RpcMetadata rpcMetadata = RpcMetadata.RpcMetadataBuilder
                .rpcMetadata()
                .group("order")
                .serviceName("test")
                .version("v1.0.0")
                .build();

        RpcClientConfig rpcConfig = RpcClientConfig.RpcClientConfigBuilder
                .config()
                .timeout(1000)
//                .remoteAddress("59.110.25.17:8888")
                .metadata(rpcMetadata)
                .build();

        ClientExchange.start(Arrays.asList(rpcConfig), "nacos://59.110.25.17:8848");

        GenericInvoker invoker = GenericProxyFactory
                .factory("com.xmutca.rpc.example.api.HelloService")
                .metadata(rpcMetadata)
                .newProxyInstance();
        Object result = invoker.invoke("sayHello", new Class[]{String.class}, new Object[]{"sb"});
        System.out.println(JSON.toJSONString(result));
        System.in.read();
    }
}
