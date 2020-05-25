package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;

import java.net.InetSocketAddress;

/**
 * 客户端管理对象
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-01-03
 */
public interface Client {

    void init(RpcClientConfig rpcClientConfig, InetSocketAddress remoteAddress);

    /**
     * 连接服务器
     * @return
     */
    void connect();

    /**
     * 断开连接
     */
    void close();

    /**
     * 判断连接状态
     * @return
     */
    boolean isConnected();

    /**
     * 获取channel
     * @return
     */
    TransportChannel getChannel();

    /**
     * 发送消息
     * @param rpcRequest
     * @return
     */
    RpcResponse send(RpcRequest rpcRequest);
}
