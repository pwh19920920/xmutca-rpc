package com.xmutca.rpc.core.consumer.cluster;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 广播调用
 * 所有提供逐个调用，任意一台报错则报错。通常用于更新提供方本地状态 速度慢，任意一台报错则报错 。
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/27
 */
@Slf4j
public class BroadcastClusterInvoker extends AbstractClusterInvoker {

    @Override
    protected RpcResponse doInvoke(RpcRequest rpcRequest, RpcClientConfig rpcClientConfig, ClientGroup groups) {
        RpcResponse result = null;
        RpcException exception = null;
        for (Client client : groups) {
            try {
                result = client.send(rpcRequest);
            } catch (RpcException e) {
                exception = e;
                log.warn(e.getMessage(), e);
            } catch (Exception e) {
                exception = new RpcException(e.getMessage(), e);
                log.warn(e.getMessage(), e);
            }
        }
        if (exception != null) {
            throw exception;
        }
        return result;
    }
}
