package com.xmutca.rpc.core.config;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.provider.ProviderEntry;
import com.xmutca.rpc.core.provider.ProviderWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-30
 */
@Slf4j
@Getter
@Setter
public class RpcServerConfig extends RpcConfig {

    /**
     * 核心线程数
     */
    private int corePoolSize = Constants.DEFAULT_CORE_POOL_SIZE;

    /**
     * 最大线程数
     */
    private int maxPoolSize = Constants.DEFAULT_MAX_POOL_SIZE;

    /**
     * 传输实现
     */
    private String transporterType = Constants.DEFAULT_TRANSPORTER;

    /**
     * 端口
     */
    private int port = Constants.DEFAULT_PORT;

    /**
     * 方法配置map
     * key: 类 + 方法
     * value: 具体配置
     */
    private Map<String, RpcMethodConfig> methodConfigMap = new ConcurrentHashMap<>(16);

    /**
     * 元数据
     */
    private RpcMetadata metadata = RpcMetadata.getDefault();

    /**
     * 扫描包
     */
    private String scanPackage = Constants.DEFAULT_PACKAGE;

    public RpcServerConfig() {

    }

    public RpcServerConfig(Class<?> bootstrapClazz) {
        this.scanPackage = bootstrapClazz.getPackage().getName();
    }

    public RpcServerConfig(String basePackage) {
        this.scanPackage = basePackage;
    }

    /**
     * 通过具体信息获取配置
     * @param key
     * @return
     */
    public int getTimeout(String key) {
        RpcMethodConfig methodConfig = methodConfigMap.get(key);
        if (null == methodConfig) {
            return getTimeout();
        }

        return methodConfig.getTimeout();
    }

    /**
     * 提供者列表
     * @return
     */
    public List<ProviderEntry> getProviders() {
        return ProviderWrapper.load(this.scanPackage);
    }
}