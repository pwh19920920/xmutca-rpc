package com.xmutca.rpc.core.rpc.registry;

import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.config.RpcServerConfig;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
public interface RegistryFactory {

    /**
     * 注册
     * @param serverConfig
     * @param registryAddress
     * @throws Exception
     */
    void registry(RpcServerConfig serverConfig, String registryAddress) throws Exception;

    /**
     * 发现
     * @param metadataList
     * @param registryAddress
     * @throws Exception
     */
    void subscribe(List<RpcMetadata> metadataList, String registryAddress) throws Exception;
}
