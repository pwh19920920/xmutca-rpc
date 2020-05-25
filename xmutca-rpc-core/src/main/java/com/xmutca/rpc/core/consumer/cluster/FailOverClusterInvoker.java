package com.xmutca.rpc.core.consumer.cluster;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-08
 */
@Slf4j
public class FailOverClusterInvoker extends AbstractClusterInvoker {

    @Override
    protected RpcResponse doInvoke(RpcRequest rpcRequest, RpcClientConfig rpcClientConfig, List<ClientGroup> groups) {
        int retries = rpcClientConfig.getRetries();
        return doExecute(0, retries, rpcRequest, groups);
    }

    /**
     * 执行
     * @param curr
     * @param max
     * @param rpcRequest
     * @param groups
     * @return
     */
    public RpcResponse doExecute(int curr, int max, RpcRequest rpcRequest, List<ClientGroup> groups) {
        RpcException lastEx;

        try {
            Client client = select(groups);
            return client.send(rpcRequest);
        } catch (RpcException ex) {
            lastEx = ex;
            log.info("FailOverClusterInvoker current exec times: {}, max retry times: {}", curr, max);
        } finally {
            curr++;
        }

        if (curr > max) {
            throw lastEx;
        }

        return doExecute(curr, max, rpcRequest, groups);
    }
}
