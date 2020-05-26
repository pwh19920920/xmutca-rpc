package com.xmutca.rpc.core.rpc.exchange;

import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.config.RpcServerConfig;
import com.xmutca.rpc.core.provider.ProviderEntry;
import com.xmutca.rpc.core.rpc.registry.RegistryWrapper;
import com.xmutca.rpc.core.transport.Server;

/**
 * 启动入口，包含服务端，客户端
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-17
 */
public class ServerExchange {

    private ServerExchange() {

    }

    /**
     * 启动服务
     * @param serverConfig
     * @param registry
     * @throws InterruptedException
     */
    public static void start(RpcServerConfig serverConfig, String registry) throws InterruptedException {
        Server server = ExtensionLoader.getExtensionLoader(Server.class).getExtension(serverConfig.getTransporterType());
        server.init(serverConfig);

        // 发布
        for (ProviderEntry entry: serverConfig.getProviders()) {
            server.publish(entry.getInterfaceName(), entry.getProvider());
        }

        // 注册 + 监听 + 心跳
        RegistryWrapper.register(serverConfig, registry);

        // 绑定
        server.bind();
    }
}
