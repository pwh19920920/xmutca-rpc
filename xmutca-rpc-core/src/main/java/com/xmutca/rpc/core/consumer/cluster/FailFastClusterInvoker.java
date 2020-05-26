package com.xmutca.rpc.core.consumer.cluster;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-08
 */
public class FailFastClusterInvoker extends AbstractClusterInvoker {

    @Override
    protected RpcResponse doInvoke(RpcRequest rpcRequest, RpcClientConfig rpcClientConfig, ClientGroup groups) {
        Client client = select(groups);
        return client.send(rpcRequest);
    }
}
