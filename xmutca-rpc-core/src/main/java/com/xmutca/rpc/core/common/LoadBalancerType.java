package com.xmutca.rpc.core.common;

/**
 * 负载均衡
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-10
 */
public enum LoadBalancerType {

    /**
     * 轮询
     */
    LOAD_BALANCER_TYPE_POLLING("polling"),

    /**
     * 随机
     */
    LOAD_BALANCER_TYPE_RANDOM("random");

    private String type;

    LoadBalancerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
