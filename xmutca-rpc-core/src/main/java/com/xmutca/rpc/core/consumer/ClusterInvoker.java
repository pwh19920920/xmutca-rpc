package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;

/**
 * 集群策略
 * @author qudian
 */
public interface ClusterInvoker extends GenericInvoker {

    /**
     * 初始化
     * @param rpcMetadata
     * @param loadBalancer
     * @param rpcClientConfig
     * @param className
     */
    void init(RpcMetadata rpcMetadata, LoadBalancer loadBalancer, RpcClientConfig rpcClientConfig, String className);
}
