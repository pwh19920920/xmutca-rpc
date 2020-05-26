package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;

import java.util.List;

/**
 * 负载均衡
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-06
 */
public interface LoadBalancer {

    /**
     * 负载均衡， appName + group + version
     * @param groups
     * @return
     */
    Client select(ClientGroup groups);
}
