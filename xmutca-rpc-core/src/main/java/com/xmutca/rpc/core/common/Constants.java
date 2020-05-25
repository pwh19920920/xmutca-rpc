package com.xmutca.rpc.core.common;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-02
 */
public final class Constants {

    private Constants() {}

    /**
     * 长度位0
     */
    public static final int ZERO_LENGTH = 0;

    /**
     * 默认写超时时间
     */
    public static final int DEFAULT_WRITER_IDLE_TIME_SECONDS = 20;

    /**
     * 默认读超时时间
     */
    public static final int DEFAULT_READER_IDLE_TIME_SECONDS = 60;

    /**
     * 默认线程数
     */
    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    /**
     * 默认连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 3000;

    /**
     * 重新连接次数
     */
    public static final int DEFAULT_RECONNECT = 2000;

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_TIMEOUT = 1000;

    /**
     * 默认失败重试次数
     */
    public static final int DEFAULT_RETRIES = 2;

    /**
     * 默认负载均衡算法
     */
    public static final String DEFAULT_LOAD_BALANCER = LoadBalancerType.LOAD_BALANCER_TYPE_RANDOM.getType();

    /**
     * 默认容错算法
     */
    public static final String DEFAULT_CLUSTER = ClusterType.CLUSTER_TYPE_FAIL_OVER.getType();

    /**
     * 默认传输实现
     */
    public static final String DEFAULT_TRANSPORTER = TransporterType.TRANSPORTER_TYPE_NETTY.getName();

    /**
     * 默认分组
     */
    public static final String DEFAULT_META_DATA_GROUP = "default";

    /**
     * 默认版本号
     */
    public static final String DEFAULT_META_DATA_VERSION = "v1.0.0";

    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 21880;

    /**
     * 默认核心线程数
     */
    public static final int DEFAULT_CORE_POOL_SIZE = 60;

    /**
     * 默认最大线程数
     */
    public static final int DEFAULT_MAX_POOL_SIZE = 300;

    /**
     * 默认扫描包地址
     */
    public static final String DEFAULT_PACKAGE = "com";
}
