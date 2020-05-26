package com.xmutca.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.rpc.registry.RegistryFactory;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
public class NacosHolder {

    private NacosHolder() {

    }

    private static NamingService namingService;
    private static final Lock LOCK = new ReentrantLock();

    /**
     * 获取命名服务
     * @param address
     * @return
     */
    public static NamingService getNaming(String address) throws NacosException {
        if (Objects.nonNull(namingService)) {
            return namingService;
        }

        LOCK.lock();
        try {
            if (Objects.nonNull(namingService)) {
                return namingService;
            }

            namingService = NamingFactory.createNamingService(address);
            return namingService;
        } finally {
            LOCK.unlock();
        }
    }
}
