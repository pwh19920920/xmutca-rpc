package com.xmutca.rpc.registry.nacos;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.common.NetUtils;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.config.RpcServerConfig;
import com.xmutca.rpc.core.consumer.ConsumerConfigHolder;
import com.xmutca.rpc.core.rpc.registry.RegistryFactory;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientPool;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
@Slf4j
public class NacosRegistryFactory implements RegistryFactory {

    @Override
    public void registry(RpcServerConfig serverConfig, String registryAddress) throws Exception {
        // 获取服务
        RpcMetadata metadata = serverConfig.getMetadata();
        NamingService naming = NacosHolder.getNaming(registryAddress);

        // 实例参数
        Instance instance = new Instance();
        instance.setIp(NetUtils.getLocalHost());
        instance.setPort(serverConfig.getPort());
        instance.addMetadata(Constants.REGISTER_SERVICE_NAME, metadata.getUniqueMetaName());

        // 发布注册
        naming.registerInstance(metadata.getUniqueMetaName(), metadata.getGroup(), instance);
    }

    @Override
    public void subscribe(List<RpcMetadata> metadataList, String registryAddress) throws Exception {
        // metadataMap这部分需要利用元数据发现并连接 + 监听 + 心跳
        NamingService naming = NacosHolder.getNaming(registryAddress);

        for (RpcMetadata meta : metadataList) {
            // 对已经上线的进行配置化
            List<Instance> instances = naming.selectInstances(meta.getUniqueMetaName(), meta.getGroup(), true);
            batchCheckAndConnect(instances);

            // 监听事件，针对未来变化
            naming.subscribe(meta.getUniqueMetaName(), meta.getGroup(), event -> {
                // 非NamingEvent事件，直接返回
                if (!(event instanceof NamingEvent)) {
                    return;
                }

                // 批量检查并连接
                batchCheckAndConnect(((NamingEvent) event).getInstances());
            });
        }
    }

    /**
     * 批量检查并连接
     * @param instances
     */
    private static void batchCheckAndConnect(List<Instance> instances) {
        for (Instance instance : instances) {
            checkAndConnect(instance);
        }
    }

    /**
     * 检查并连接
     * @param instance
     */
    private static void checkAndConnect(Instance instance) {
        // 活跃 - 检测是否已连接，未连接则进行连接
        if (instance.isHealthy()) {
            Client client = ClientPool.getClient(instance.getIp(), instance.getPort());
            if (Objects.nonNull(client)) {
                log.info("xmutca rpc client connect is active, ip: {}, port: {}", instance.getIp(), instance.getPort());
                return;
            }

            connect(instance);
            return;
        }

        // 非活跃，则检查是否仍在连接状态，进行remove
        Client client = ClientPool.getClient(instance.getIp(), instance.getPort());
        if (Objects.isNull(client)) {
            log.info("xmutca rpc client connect to disconnect success, ip: {}, port: {}", instance.getIp(), instance.getPort());
            return;
        }

        // 判断连接
        log.info("xmutca rpc client connect to disconnect start, ip: {}, port: {}", instance.getIp(), instance.getPort());
        client.close();
    }

    /**
     * 连接
     * @param instance
     */
    private static void connect(Instance instance) {
        // 获取配置并注册
        Map<String, String> metadata = instance.getMetadata();
        String uniqueName = metadata.get(Constants.REGISTER_SERVICE_NAME);
        if (Objects.isNull(uniqueName)) {
            return;
        }

        RpcClientConfig config = ConsumerConfigHolder.get(uniqueName);
        if (Objects.isNull(config)) {
            return;
        }

        log.info("xmutca rpc client connect to server start, ip: {}, port: {}", instance.getIp(), instance.getPort());
        Client client = ExtensionLoader.getExtensionLoader(Client.class).newInstance(config.getTransporterType());
        client.init(config, new InetSocketAddress(instance.getIp(), instance.getPort()));
    }
}
