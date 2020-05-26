package com.xmutca.rpc.spring.common;

import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.config.RpcServerConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
@Getter
@Setter
@Component
@ConfigurationProperties("rpc")
public class XmutcaRpcProperties {

    /**
     * 注册地址
     */
    private String registry;

    /**
     * consumer配置
     */
    private List<RpcClientConfig> consumer;

    /**
     * provider配置
     */
    private RpcServerConfig provider;
}
