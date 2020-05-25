package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.config.RpcMetadata;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * appName + group + version
 * 得到一个List<ClientGroup>, 再进行负载均衡算法
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-03
 */
public class ClientPool {

    private ClientPool() {}

    private static final Map<String, CopyOnWriteArrayList<ClientGroup>> metaDataGroupMap = new ConcurrentHashMap<>();

    private static final Map<InetSocketAddress, ClientGroup> remoteGroupMap = new ConcurrentHashMap<>();

    private static Lock lock = new ReentrantLock();

    /**
     * appName + group + version
     * 得到一个客户端List<ClientGroup>
     * @param rpcMetadata
     * @return
     */
    public static List<ClientGroup> filter(RpcMetadata rpcMetadata) {
        return metaDataGroupMap.getOrDefault(rpcMetadata.getUniqueMetaName(), new CopyOnWriteArrayList<>());
    }

    /**
     * 返回元组
     * @return
     */
    public static Map<String, CopyOnWriteArrayList<ClientGroup>> getMetaDataGroupMap() {
        return metaDataGroupMap;
    }

    /**
     * 添加客户端
     * @param metadata
     * @param remoteAddress
     * @param client
     */
    public static void addClient(RpcMetadata metadata, InetSocketAddress remoteAddress, Client client) {
        lock.lock();
        try {
            ClientGroup remoteGroup = remoteGroupMap.getOrDefault(remoteAddress, new ClientGroup());
            CopyOnWriteArrayList<ClientGroup> metaDataGroups = metaDataGroupMap.getOrDefault(metadata.getUniqueMetaName(), new CopyOnWriteArrayList<>());

            // 添加客户端
            remoteGroup.addIfAbsent(client);
            metaDataGroups.addIfAbsent(remoteGroup);

            // 返回数据
            remoteGroupMap.putIfAbsent(remoteAddress, remoteGroup);
            metaDataGroupMap.putIfAbsent(metadata.getUniqueMetaName(), metaDataGroups);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 移除客户端
     * @param metadata
     * @param remoteAddress
     * @param client
     */
    public static void removeClient(RpcMetadata metadata, InetSocketAddress remoteAddress, Client client) {
        lock.lock();
        try {
            ClientGroup remoteGroup = remoteGroupMap.getOrDefault(remoteAddress, new ClientGroup());
            CopyOnWriteArrayList<ClientGroup> metaDataGroups = metaDataGroupMap.getOrDefault(metadata.getUniqueMetaName(), new CopyOnWriteArrayList<>());

            // 移除无效客户端
            remoteGroup.remove(client);

            // 如果为空，需要移除
            if (remoteGroup.isEmpty()) {
                remoteGroupMap.remove(remoteAddress);
                metaDataGroups.remove(remoteGroup);
            }
        } finally {
            lock.unlock();
        }
    }
}
