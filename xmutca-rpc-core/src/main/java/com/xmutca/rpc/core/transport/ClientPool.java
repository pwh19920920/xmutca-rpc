package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.config.RpcMetadata;
import lombok.extern.slf4j.Slf4j;

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

    private static final Map<String, ClientGroup> metaDataGroupMap = new ConcurrentHashMap<>();

    private static final Map<String, Client> remoteGroupMap = new ConcurrentHashMap<>();

    private static Lock lock = new ReentrantLock();

    /**
     * appName + group + version
     * 得到一个客户端List<ClientGroup>
     * @param rpcMetadata
     * @return
     */
    public static ClientGroup filter(RpcMetadata rpcMetadata) {
        return metaDataGroupMap.getOrDefault(rpcMetadata.getUniqueMetaName(), new ClientGroup());
    }

    /**
     * 返回元组
     * @return
     */
    public static Map<String, ClientGroup> getMetaDataGroupMap() {
        return metaDataGroupMap;
    }

    /**
     * 返回信息
     * @return
     */
    public static Map<String, Client> getRemoteGroupMap() {
        return remoteGroupMap;
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
            String address = getRemoteAddress(remoteAddress);
            ClientGroup metaDataGroups = metaDataGroupMap.getOrDefault(metadata.getUniqueMetaName(), new ClientGroup());

            // 添加客户端
            remoteGroupMap.putIfAbsent(address, client);
            metaDataGroups.addIfAbsent(client);

            // 返回到map
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
            String address = getRemoteAddress(remoteAddress);
            ClientGroup metaDataGroups = metaDataGroupMap.getOrDefault(metadata.getUniqueMetaName(), new ClientGroup());

            // 移除无效客户端
            remoteGroupMap.remove(address);
            metaDataGroups.remove(client);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取地址
     * @param remoteAddress
     * @return
     */
    private static String getRemoteAddress(InetSocketAddress remoteAddress) {
        return String.format("%s:%s", remoteAddress.getHostString(), remoteAddress.getPort());
    }

    /**
     * 获取地址
     * @param host
     * @param port
     * @return
     */
    private static String getRemoteAddress(String host, int port) {
        return String.format("%s:%s", host, port);
    }

    /**
     * 检查远程地址是否已连接
     * @param address
     * @return
     */
    public static boolean checkRemoteAddress(String address) {
        return remoteGroupMap.get(address) != null;
    }

    /**
     * 检查远程地址是否已连接
     * @param host
     * @param port
     * @return
     */
    public static boolean checkRemoteAddress(String host, int port) {
        return remoteGroupMap.get(getRemoteAddress(host, port)) != null;
    }

    /**
     * 获取客户端
     * @param host
     * @param port
     * @return
     */
    public static Client getClient(String host, int port) {
        return remoteGroupMap.get(getRemoteAddress(host, port));
    }
}
