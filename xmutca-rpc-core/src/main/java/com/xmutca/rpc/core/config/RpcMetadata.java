package com.xmutca.rpc.core.config;

import com.xmutca.rpc.core.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-03
 */
@Getter
@Setter
public class RpcMetadata {

    /**
     * 系统元数据 - 应用名称
     */
    private String serviceName;

    /**
     * 系统元数据 - 版本号
     */
    private String version = Constants.DEFAULT_META_DATA_VERSION;

    /**
     * 系统元数据 - 系统分组
     */
    private String group = Constants.DEFAULT_META_DATA_GROUP;

    /**
     * 获取唯一元名称
     * @return
     */
    public String getUniqueMetaName() {
        return String.format("%s:%s:%s", serviceName, group, version);
    }

    /**
     * builder类
     */
    public static final class RpcMetadataBuilder {
        private String serviceName;
        private String version;
        private String group;

        private RpcMetadataBuilder() {
        }

        public static RpcMetadataBuilder rpcMetadata() {
            return new RpcMetadataBuilder();
        }

        public RpcMetadataBuilder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RpcMetadataBuilder version(String version) {
            this.version = version;
            return this;
        }

        public RpcMetadataBuilder group(String group) {
            this.group = group;
            return this;
        }

        public RpcMetadata build() {
            RpcMetadata rpcMetadata = new RpcMetadata();
            rpcMetadata.setServiceName(serviceName);
            rpcMetadata.setVersion(version);
            rpcMetadata.setGroup(group);
            return rpcMetadata;
        }
    }

    /**
     * 获取默认
     * @return
     */
    public static RpcMetadata getDefault() {
        return new RpcMetadata();
    }
}
