package com.xmutca.rpc.test;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.consumer.GenericInvoker;
import com.xmutca.rpc.core.consumer.GenericProxyFactory;
import com.xmutca.rpc.core.rpc.exchange.ClientExchange;
import com.xmutca.rpc.core.transport.ClientPool;
import com.xmutca.rpc.core.transport.netty.client.NettyClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-31
 */
public class ClientTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        RpcMetadata rpcMetadata = RpcMetadata.RpcMetadataBuilder
                .rpcMetadata()
                .group("order")
                .serviceName("test")
                .version("v1.0.0")
                .build();
        InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 8888);

        RpcClientConfig rpcConfig = RpcClientConfig.RpcClientConfigBuilder
                .config()
                .timeout(1000)
                .remoteAddress("remoteAddress")
                .metadata(rpcMetadata)
                .build();

        ClientExchange.start(Arrays.asList(rpcConfig));

//        for (int i = 0; i < 100; i++) {
//            Thread.sleep(20000);
//            try{
//                RpcRequest req = new RpcRequest();
//                req.setClassName("class");
//                req.setMethodName("method");
//                req.setTransportType(TransportType.MODE_REQ_TWO_WAY);
//                RpcResponse resp1 = nettyClient.send(req);
//                System.out.println(resp1.getResult());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        System.out.println(ClientPool.getMetaDataGroupMap());
        GenericInvoker invoker = GenericProxyFactory
                .factory(ClientTest.class)
                .metadata(rpcMetadata)
                .newProxyInstance();
        Object result = invoker.invoke("xxx", new Class[]{}, null);
        System.out.println(JSON.toJSONString(result));
        System.in.read();
    }
}
