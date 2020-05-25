package com.xmutca.rpc.core.consumer.loadbalance;

import com.xmutca.rpc.core.transport.ClientGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮循负载均衡
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-06
 */
public class PollingLoadBalancer extends AbstractLoadBalancer {

    /**
     * 索引
     */
    private AtomicInteger index = new AtomicInteger();

    /**
     * 获取实例
     * @return
     */
    public static PollingLoadBalancer getInstance() {
        // round-robin是有状态的, 不能是单例
        return new PollingLoadBalancer();
    }

    @Override
    protected ClientGroup doSelect(List<ClientGroup> groups) {
        return groups.get(index.getAndIncrement() % groups.size());
    }
}
