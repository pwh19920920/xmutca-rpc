package com.xmutca.rpc.core.rpc.filter;

import com.xmutca.rpc.core.common.ExtensionGroup;
import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.rpc.*;
import com.xmutca.rpc.core.rpc.invoke.Invoker;
import com.xmutca.rpc.core.rpc.invoke.RpcResponseInvoke;

import java.util.Collection;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-31
 */
public class FilterWrapper {

    /**
     * 执行链，最后一环是真正的执行方法，但是任意一环都可以提前退出
     * @param invoker
     * @param group
     * @return
     */
    public static Invoker<RpcRequest, RpcResponse> buildInvokeChain(Invoker<RpcRequest, RpcResponse> invoker, ExtensionGroup group) {
        Invoker<RpcRequest, RpcResponse> last = invoker;

        // 初始化chain的过程
        Collection<Filter> invokers = ExtensionLoader.getExtensionLoader(Filter.class).getAllExtensionOfSorted(group);
        for (Filter filter:invokers) {
            Invoker<RpcRequest, RpcResponse> next = last;
            last = rpcRequest -> filter.invoke(next, rpcRequest);
        }
        return last;
    }

    public static void main(String[] args) {
        RpcResponseInvoke rpcResponseInvoke = new RpcResponseInvoke();
        Invoker<RpcRequest, RpcResponse> invoker = FilterWrapper.buildInvokeChain(rpcResponseInvoke, ExtensionGroup.CONSUMER);
        RpcResponse resp = invoker.invoke(new RpcRequest());
    }
}
