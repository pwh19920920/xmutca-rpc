package com.xmutca.rpc.spring.provider;

import com.xmutca.rpc.core.rpc.exchange.ServerExchange;
import com.xmutca.rpc.spring.common.XmutcaRpcProperties;
import org.springframework.boot.CommandLineRunner;

import java.util.Objects;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
public class ProviderCommand implements CommandLineRunner {

    private XmutcaRpcProperties xmutcaRpcProperties;

    public ProviderCommand(XmutcaRpcProperties xmutcaRpcProperties) {
        this.xmutcaRpcProperties = xmutcaRpcProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Objects.isNull(xmutcaRpcProperties.getProvider())) {
            return;
        }

        // 启动提供者容器
        ServerExchange.start(xmutcaRpcProperties.getProvider());
    }
}
