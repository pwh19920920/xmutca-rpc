package com.xmutca.rpc.core.common;

/**
 * 容错策略
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-10
 */
public enum ClusterType {

    /**
     * 快速失败
     */
    CLUSTER_TYPE_FAIL_FAST("failFast"),

    /**
     * 失败重试
     */
    CLUSTER_TYPE_FAIL_OVER("failOver"),

    /**
     * 失败安全
     */
    CLUSTER_TYPE_FAIL_SAFE("failSafe");

    private String type;

    ClusterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
