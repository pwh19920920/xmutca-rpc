package com.xmutca.rpc.consumer.filter;

import com.xmutca.rpc.core.common.ExtensionGroup;
import com.xmutca.rpc.core.common.ExtensionWrapper;
import com.xmutca.rpc.core.rpc.filter.Filter;
import com.xmutca.rpc.core.rpc.invoke.Invoker;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-01
 */
@ExtensionWrapper(group = ExtensionGroup.ALL, order = 1001)
public class TestFiler1 implements Filter {

    @Override
    public RpcResponse invoke(Invoker<RpcRequest, RpcResponse> invoker, RpcRequest rpcRequest) {
        System.out.println("TestFiler1 in");
        RpcResponse resp = invoker.invoke(rpcRequest);
        System.out.println("TestFiler1 out");
        return resp;
    }
}
