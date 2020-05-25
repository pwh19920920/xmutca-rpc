package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.config.RpcServerConfig;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
public interface Server {

    /**
     * 初始化
     * @param rpcServerConfig
     */
    void init(RpcServerConfig rpcServerConfig);

    /**
     * 启动
     * @throws InterruptedException
     */
    void bind() throws InterruptedException;

    /**
     * 发布
     * @param interfaceName
     * @param object
     */
    void publish(String interfaceName, Object object);

    /**
     * 关闭
     */
    void stop();
}
