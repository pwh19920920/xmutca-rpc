package com.xmutca.rpc.core.consumer.loadbalance;

import com.xmutca.rpc.core.consumer.LoadBalancer;
import com.xmutca.rpc.core.transport.ClientGroup;

import java.util.List;

/**
 * 负载均衡
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-09
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    @Override
    public ClientGroup select(List<ClientGroup> groups) {
        // 判空
        if (null == groups || groups.isEmpty()) {
            return null;
        }

        // 只有一个的话，直接返回
        if (groups.size() == 1) {
            return groups.get(0);
        }
        return doSelect(groups);
    }

    /**
     * 负载均衡
     *
     * @param groups
     * @return
     */
    protected abstract ClientGroup doSelect(List<ClientGroup> groups);
}
