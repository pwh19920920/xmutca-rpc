package com.xmutca.rpc.core.transport;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-03
 */
public class ClientGroup extends CopyOnWriteArrayList<Client> {

    /**
     * 索引
     */
    private AtomicInteger index = new AtomicInteger();

    /**
     * 轮询获取客户端
     * @return
     */
    public Client next() {
        if (this.isEmpty()) {
            return null;
        }

        return this.get(index.getAndIncrement() % this.size());
    }
}
