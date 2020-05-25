package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.common.ExtensionGroup;
import com.xmutca.rpc.core.config.RpcServerConfig;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.rpc.filter.FilterWrapper;
import com.xmutca.rpc.core.rpc.invoke.Invoker;
import com.xmutca.rpc.core.rpc.invoke.RpcResponseInvoke;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-03
 */
public abstract class AbstractServer implements Server {

    private RpcServerConfig rpcServerConfig;
    private ThreadPoolExecutor threadPoolExecutor;
    private Invoker<RpcRequest, RpcResponse> invoker = FilterWrapper.buildInvokeChain(new RpcResponseInvoke(), ExtensionGroup.PROVIDER);

    @Override
    public void init(RpcServerConfig rpcServerConfig) {
        this.rpcServerConfig = rpcServerConfig;

        // 初始化
        doOpen();

        // 初始化线程池
        threadPoolExecutor = new ThreadPoolExecutor(
                rpcServerConfig.getCorePoolSize(),
                rpcServerConfig.getMaxPoolSize(),
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new DefaultThreadFactory("ServerExecute", true));
    }

    @Override
    public void publish(String interfaceName, Object object) {
        if (null == object) {
            return;
        }

        // 加入提供者列表
        RpcResponseInvoke.addProvider(interfaceName, object);
    }

    /**
     * 创建连接
     */
    protected abstract void doOpen();

    /**
     * 获取执行器
     * @return
     */
    public Invoker<RpcRequest, RpcResponse> getInvoker() {
        return invoker;
    }

    /**
     * 获取线程池
     * @return
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    /**
     * 获取端口
     * @return
     */
    protected int getPort() {
        return rpcServerConfig.getPort();
    }
}
