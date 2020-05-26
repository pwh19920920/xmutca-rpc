package com.xmutca.rpc.core.consumer.loadbalance;

import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-09
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    private static final RandomLoadBalancer instance = new RandomLoadBalancer();

    /**
     * 获取实例
     * @return
     */
    public static RandomLoadBalancer getInstance() {
        return instance;
    }

    @Override
    protected Client doSelect(ClientGroup groups) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return groups.get(random.nextInt(groups.size()));
    }
}
