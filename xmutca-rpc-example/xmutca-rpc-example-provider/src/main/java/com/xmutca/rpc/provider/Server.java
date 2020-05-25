package com.xmutca.rpc.provider;

import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.config.RpcServerConfig;
import com.xmutca.rpc.core.rpc.exchange.ServerExchange;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/21
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        RpcServerConfig rpcConfig = new RpcServerConfig(Server.class);
        rpcConfig.setCorePoolSize(60);
        rpcConfig.setMaxPoolSize(300);
        rpcConfig.setPort(8888);
        RpcMetadata rpcMetadata = RpcMetadata.RpcMetadataBuilder
                .rpcMetadata()
                .group("order")
                .serviceName("test")
                .version("v1.0.0")
                .build();
        rpcConfig.setMetadata(rpcMetadata);

        // 具体实现从服务里获取，然后创建实例
        ServerExchange.start(rpcConfig);
    }
}
