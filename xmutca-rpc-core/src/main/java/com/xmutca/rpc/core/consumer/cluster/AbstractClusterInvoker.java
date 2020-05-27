package com.xmutca.rpc.core.consumer.cluster;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.consumer.ClusterInvoker;
import com.xmutca.rpc.core.consumer.LoadBalancer;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;
import com.xmutca.rpc.core.transport.ClientPool;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-08
 */
public abstract class AbstractClusterInvoker implements ClusterInvoker {

    /**
     * 元数据
     */
    private RpcMetadata rpcMetadata;

    /**
     * 负载均衡
     */
    private LoadBalancer loadBalancer;

    /**
     * 本地配置
     */
    private RpcClientConfig rpcClientConfig;

    /**
     * 执行的facade名
     */
    private String className;

    /**
     * 初始化相关对象
     * @param rpcMetadata
     * @param loadBalancer
     * @param rpcClientConfig
     * @param className
     */
    @Override
    public void init(RpcMetadata rpcMetadata, LoadBalancer loadBalancer, RpcClientConfig rpcClientConfig, String className) {
        this.rpcMetadata = rpcMetadata;
        this.loadBalancer = loadBalancer;
        this.rpcClientConfig = rpcClientConfig;
        this.className = className;
    }

    /**
     * 执行发送
     * @param method
     * @param parameterTypes
     * @param args
     * @return
     */
    @Override
    public Object invoke(String method, String[] parameterTypes, Object[] args) {
        return invoke(method, convertParameterType(parameterTypes), args);
    }

    @Override
    public Object invoke(RpcRequest rpcRequest) {
        ClientGroup groups = filter();
        if (groups.isEmpty()) {
            throw new RpcException("Failed to invoke the method, No provider available for the service " + rpcRequest.getFullName());
        }
        return doInvoke(rpcRequest, rpcClientConfig, groups).getResult();
    }

    @Override
    public Object invoke(String method, Class<?>[] parameterTypes, Object[] args) {
        RpcRequest rpcRequest = RpcRequest
                .RpcRequestBuilder
                .rpcRequest()
                .className(className)
                .methodName(method)
                .parameterTypes(parameterTypes)
                .arguments(defaultArguments(args))
                .build();
        return invoke(rpcRequest);
    }

    /**
     * 处理空参数问题
     * @param args
     * @return
     */
    private Object[] defaultArguments(Object[] args) {
        if (null == args || args.length == 0) {
            return new Object[]{};
        }
        return args;
    }

    /**
     * 转换参数类型
     * @param parameterTypes
     * @return
     */
    private Class<?>[] convertParameterType(String[] parameterTypes) {
        if (null == parameterTypes || parameterTypes.length == 0) {
            return new Class<?>[]{};
        }

        Class<?>[] types = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                types[i] = Class.forName(parameterTypes[i]);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("ClassNotFoundException:" + parameterTypes[i], e);
            }
        }

        return types;
    }

    /**
     * 执行
     * @param rpcRequest
     * @param rpcClientConfig
     * @param groups
     * @return
     */
    protected abstract RpcResponse doInvoke(RpcRequest rpcRequest, RpcClientConfig rpcClientConfig, ClientGroup groups);

    /**
     * 目录搜索
     *
     * @return
     */
    protected ClientGroup filter() {
        return ClientPool.filter(rpcMetadata);
    }

    /**
     * 负载均衡
     *
     * @return
     */
    protected Client select(ClientGroup groups) {
        // 负载均衡
        Client client = loadBalancer.select(groups);
        if (null == client) {
            throw new RpcException("fail to send, not channel active");
        }
        return client;
    }
}
