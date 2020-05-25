package com.xmutca.rpc.core.transport;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-01
 */
public interface TransportChannel {

    /**
     * 发送消息，发送失败将会抛出异常
     * @param transporter
     */
    void send(Transporter transporter);

    /**
     * 是否连接
     * @return
     */
    boolean isConnected();

    /**
     * 关闭连接
     * @return
     */
    void close();
}
