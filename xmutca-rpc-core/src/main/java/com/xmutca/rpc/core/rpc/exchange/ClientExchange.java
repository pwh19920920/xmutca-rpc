package com.xmutca.rpc.core.rpc.exchange;

import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.consumer.ConsumerConfigHolder;
import com.xmutca.rpc.core.transport.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-17
 */
public class ClientExchange {

    private ClientExchange() {

    }

    /**
     * init
     * @param rpcClientConfigs
     */
    public static void start(List<RpcClientConfig> rpcClientConfigs) {
        Map<String, RpcMetadata> metadataMap = new ConcurrentHashMap<>(16);
        List<RpcClientConfig> directUrls = new ArrayList<>(16);

        // 过滤直接客户端
        for (RpcClientConfig rpcClientConfig : rpcClientConfigs) {
            // putClientConfigHolder
            ConsumerConfigHolder.put(rpcClientConfig.getMetadata().getUniqueMetaName(), rpcClientConfig);

            // 配置了直连地址的
            if (null != rpcClientConfig.getRemoteAddress()) {
                directUrls.add(rpcClientConfig);
                continue;
            }

            // 需要从注册中心读取的数据元组
            metadataMap.putIfAbsent(rpcClientConfig.getMetadata().getUniqueMetaName(), rpcClientConfig.getMetadata());
        }

        // 启动直连客户端
        for (RpcClientConfig clientConfig : directUrls) {
            Client client = ExtensionLoader.getExtensionLoader(Client.class).getExtension(clientConfig.getTransporterType());
            client.init(clientConfig, clientConfig.getRemoteAddress());
        }

        // metadataMap这部分需要利用元数据发现并连接 + 监听 + 心跳
    }
}
