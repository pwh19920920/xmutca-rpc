package com.xmutca.rpc.core.config;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.serialize.CodecType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-30
 */
@Getter
@Setter
public class RpcClientConfig extends RpcConfig {

    public static final int ADDRESS_LENGTH = 2;

    /**
     * 启动检查
     */
    private boolean check = true;

    /**
     * 连接url
     */
    private String remoteAddress;

    /**
     * 序列化类型
     */
    private String codecType = CodecType.CODEC_TYPE_PROTO_STUFF.getSimpleName();

    /**
     * 传输实现
     */
    private String transporterType = Constants.DEFAULT_TRANSPORTER;

    /**
     * 元数据
     */
    private RpcMetadata metadata = RpcMetadata.getDefault();

    /**
     * 方法配置map
     * key：类.方法
     */
    private Map<String, RpcMethodConfig> methodConfigMap = new ConcurrentHashMap<>(16);

    /**
     * 重试次数
     */
    private int retries = Constants.DEFAULT_RETRIES;

    /**
     * 负载均衡算法
     */
    private String loadBalancer = Constants.DEFAULT_LOAD_BALANCER;

    /**
     * 容错策略
     */
    private String cluster = Constants.DEFAULT_CLUSTER;

    /**
     * 获取序列化方式
     * @return
     */
    public CodecType getCodecType() {
        return CodecType.get(codecType);
    }

    /**
     * 设置序列化方式
     * @param codecType
     */
    public void setCodecType(String codecType) {
        this.codecType = codecType;
    }

    /**
     * 设置序列化方式
     * @param codecType
     */
    public void setCodec(CodecType codecType) {
        this.codecType = codecType.getSimpleName();
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

    public InetSocketAddress getRemoteAddress() {
        if (StringUtils.isBlank(remoteAddress)) {
            return null;
        }

        String[] address = remoteAddress.split(":");
        if (address.length != ADDRESS_LENGTH) {
            throw new RuntimeException("地址错误");
        }

        return new InetSocketAddress(address[0], Integer.parseInt(address[1]));
    }

    /**
     * builder
     */
    public static final class RpcClientConfigBuilder {
        private int timeout = Constants.DEFAULT_TIMEOUT;
        private boolean check = true;
        private String remoteAddress;
        private String codecType = CodecType.CODEC_TYPE_JSON.getSimpleName();
        private String transporterType = Constants.DEFAULT_TRANSPORTER;
        private RpcMetadata metadata = RpcMetadata.getDefault();
        private Map<String, RpcMethodConfig> methodConfigMap = new ConcurrentHashMap<>(16);
        private int retries = Constants.DEFAULT_RETRIES;
        private String loadBalancer = Constants.DEFAULT_LOAD_BALANCER;
        private String cluster = Constants.DEFAULT_CLUSTER;

        private RpcClientConfigBuilder() {
        }

        public static RpcClientConfigBuilder config() {
            return new RpcClientConfigBuilder();
        }

        public RpcClientConfigBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public RpcClientConfigBuilder check(boolean check) {
            this.check = check;
            return this;
        }

        public RpcClientConfigBuilder remoteAddress(String remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        public RpcClientConfigBuilder codecType(String codecType) {
            this.codecType = codecType;
            return this;
        }

        public RpcClientConfigBuilder codecType(CodecType codecType) {
            this.codecType = codecType.getSimpleName();
            return this;
        }

        public RpcClientConfigBuilder transporterType(String transporterType) {
            this.transporterType = transporterType;
            return this;
        }

        public RpcClientConfigBuilder metadata(RpcMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public RpcClientConfigBuilder methodConfigMap(Map<String, RpcMethodConfig> methodConfigMap) {
            this.methodConfigMap = methodConfigMap;
            return this;
        }

        public RpcClientConfigBuilder retries(int retries) {
            this.retries = retries;
            return this;
        }

        public RpcClientConfigBuilder loadBalancer(String loadBalancer) {
            this.loadBalancer = loadBalancer;
            return this;
        }

        public RpcClientConfigBuilder cluster(String cluster) {
            this.cluster = cluster;
            return this;
        }

        public RpcClientConfig build() {
            RpcClientConfig rpcClientConfig = new RpcClientConfig();
            rpcClientConfig.setTimeout(timeout);
            rpcClientConfig.setCheck(check);
            rpcClientConfig.setRemoteAddress(remoteAddress);
            rpcClientConfig.setCodecType(codecType);
            rpcClientConfig.setTransporterType(transporterType);
            rpcClientConfig.setMetadata(metadata);
            rpcClientConfig.setMethodConfigMap(methodConfigMap);
            rpcClientConfig.setRetries(retries);
            rpcClientConfig.setLoadBalancer(loadBalancer);
            rpcClientConfig.setCluster(cluster);
            return rpcClientConfig;
        }
    }
}
