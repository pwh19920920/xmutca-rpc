package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import org.assertj.core.util.Preconditions;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-10
 */
public class GenericProxyFactory {

    private GenericProxyFactory() {

    }

    /**
     * 元数据
     */
    private RpcMetadata metadata;

    /**
     * 类名
     */
    private Class<?> target;

    /**
     * get factory
     *
     * @param target
     * @return
     */
    public static GenericProxyFactory factory(Class<?> target) {
        GenericProxyFactory factory = new GenericProxyFactory();
        factory.target = target;
        return factory;
    }

    /**
     * get factory
     *
     * @param targetName
     * @return
     */
    public static GenericProxyFactory factory(String targetName) throws ClassNotFoundException {
        GenericProxyFactory factory = new GenericProxyFactory();
        factory.target = Class.forName(targetName);
        return factory;
    }

    /**
     * set metadata
     *
     * @param metadata
     * @return
     */
    public GenericProxyFactory metadata(RpcMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * 获取具体实例
     *
     * @param <T>
     * @return
     */
    public <T> T getReferenceBean() {
        GenericInvoker invoker = this.newProxyInstance();
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{target},
                (Object proxy, Method method, Object[] args) -> invoker.invoke(method.getName(), method.getParameterTypes(), args));
    }

    /**
     * 创建新代理
     *
     * @return
     */
    public GenericInvoker newProxyInstance() {
        // check metadata
        if (Objects.isNull(metadata)) {
            throw new RuntimeException("元数据配置不能为空");
        }

        RpcClientConfig rpcClientConfig = ConsumerConfigHolder.get(metadata.getUniqueMetaName());

        // check rpcClientConfig
        if (Objects.isNull(rpcClientConfig)) {
            throw new RuntimeException("客户端配置不能为空");
        }

        // new and init
        LoadBalancer loadBalancer = ExtensionLoader.getExtensionLoader(LoadBalancer.class).newInstanceForMethod(rpcClientConfig.getLoadBalancer(), "getInstance");
        ClusterInvoker cluster = ExtensionLoader.getExtensionLoader(ClusterInvoker.class).newInstance(rpcClientConfig.getCluster());
        cluster.init(rpcClientConfig.getMetadata(), loadBalancer, rpcClientConfig, target.getName());
        return cluster;
    }
}
