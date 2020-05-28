package com.xmutca.rpc.core.rpc.registry;

import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.config.RpcMetadata;
import com.xmutca.rpc.core.config.RpcServerConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @version Revision: 0.0.1
 * @author weihuang.peng
 */
@Slf4j
public class RegistryWrapper {

    private RegistryWrapper() {}

    /**
     * 加载接口实现
     * @param serverConfig 服务配置
     * @param registry 注册中心地址
     */
    public static void register(RpcServerConfig serverConfig, String registry) {
        RegistryInfo registryInfo = new RegistryInfo(registry);
        if (StringUtils.isBlank(registryInfo.getRegistryType())) {
            return;
        }

        // 获取工厂
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(registryInfo.getRegistryType());
        if (Objects.isNull(registryFactory)) {
            return;
        }

        // 注册到服务
        registryFactory.registry(serverConfig, registryInfo.getRegistryAddress());
        log.info("服务注册成功, registry for {}", registryInfo.getRegistryAddress());
    }

    /**
     * 加载接口实现
     * @param metadataList 元数据列表
     * @param registry 注册中心
     */
    public static void subscribe(List<RpcMetadata> metadataList, String registry) {
        // 元数据不存在
        if (Objects.isNull(metadataList)) {
            return;
        }

        // 转换注册信息
        RegistryInfo registryInfo = new RegistryInfo(registry);
        if (StringUtils.isBlank(registryInfo.getRegistryType())) {
            return;
        }

        // 获取工厂
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(registryInfo.getRegistryType());
        if (Objects.isNull(registryFactory)) {
            return;
        }

        // 发现服务
        registryFactory.subscribe(metadataList, registryInfo.getRegistryAddress());
        log.info("服务发现成功, registry for {}", registryInfo.getRegistryAddress());
    }

    @Getter
    @Setter
    public static class RegistryInfo {
        public static final String PROTOCOL_SPLIT = "://";
        public static final int PROTOCOL_SPLIT_SIZE = 3;

        private String registryType;
        private String registryAddress;

        public RegistryInfo(String registry) {
            if (StringUtils.isBlank(registry)) {
                return;
            }

            int schemeIndex = registry.indexOf(PROTOCOL_SPLIT);
            if (schemeIndex == 0 || registry.length() <= schemeIndex + PROTOCOL_SPLIT_SIZE) {
                return;
            }

            this.registryType = registry.substring(0, schemeIndex);
            this.registryAddress = registry.substring(schemeIndex + PROTOCOL_SPLIT_SIZE);
        }
    }
}
