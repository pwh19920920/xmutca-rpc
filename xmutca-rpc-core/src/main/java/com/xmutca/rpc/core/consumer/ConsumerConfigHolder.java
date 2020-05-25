package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.config.RpcClientConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
public class ConsumerConfigHolder {

    private ConsumerConfigHolder() {
    }

    /**
     * 客户端配置
     */
    private static Map<String, RpcClientConfig> clientConfigMap = new ConcurrentHashMap<>();

    public static void put(String fullName, RpcClientConfig clientConfig) {
        clientConfigMap.put(fullName, clientConfig);
    }

    public static RpcClientConfig get(String fullName) {
        return clientConfigMap.get(fullName);
    }

    public static Map<String, RpcClientConfig> getClientConfigMap() {
        return clientConfigMap;
    }
}
