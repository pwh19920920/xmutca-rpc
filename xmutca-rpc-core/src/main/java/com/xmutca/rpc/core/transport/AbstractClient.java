package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.common.ExtensionGroup;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.invoke.Invoker;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.invoke.RpcRequestInvoke;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.rpc.filter.FilterWrapper;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-03
 */
@Slf4j
public abstract class AbstractClient implements Client {

    private Lock connectLock = new ReentrantLock();
    private RpcRequestInvoke rpcRequestInvoke;
    private RpcClientConfig rpcClientConfig;
    private Invoker<RpcRequest, RpcResponse> invoker;
    private volatile boolean closed;
    private InetSocketAddress remoteAddress;

    @Override
    public void init(RpcClientConfig rpcClientConfig, InetSocketAddress remoteAddress) {
        this.rpcClientConfig = rpcClientConfig;
        this.rpcRequestInvoke = new RpcRequestInvoke(rpcClientConfig, this);
        this.invoker = FilterWrapper.buildInvokeChain(rpcRequestInvoke, ExtensionGroup.CONSUMER);
        this.remoteAddress = remoteAddress;

        try {
            doOpen();
        } catch (Exception ex) {
            close();
            throw new RpcException("Failed to start, cause:" + ex.getMessage(), ex);
        }

        try {
            connect();
        } catch (RpcException ex) {
            if (rpcClientConfig.isCheck()) {
                throw ex;
            }
            log.warn("Failed to start, (check == false, ignore and retry later!)", ex);
        } catch (Exception ex) {
            close();
            throw new RpcException("Failed to start, cause:" + ex.getMessage(), ex);
        }
    }

    @Override
    public void connect() {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }

            // connect
            doConnect();

            // check
            if (!isConnected()) {
                throw new RpcException("Failed connect to server");
            }

            log.info("Success connect to server");
        } catch (RpcException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RpcException("client connect server fail", ex);
        } finally {
            connectLock.unlock();
        }
    }

    /**
     * 是否存活
     * @return
     */
    @Override
    public boolean isConnected() {
        return getChannel().isConnected();
    }

    /**
     * 发送数据
     * @param rpcRequest
     * @return
     */
    @Override
    public RpcResponse send(RpcRequest rpcRequest) {
        if (!isConnected()) {
            connect();
        }

        RpcResponse resp = invoker.invoke(rpcRequest);
        if (resp.hasException()) {
            throw resp.getException();
        }

        return resp;
    }

    /**
     * 获取待连接的地址
     * @return
     */
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void close() {
        try {
            disconnect();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }

        try {
            doClose();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }

        closed = true;
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        connectLock.lock();
        try {
            try {
                TransportChannel channel = getChannel();
                if (channel != null) {
                    channel.close();
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }


            try {
                doDisConnect();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        } finally {
            connectLock.unlock();
        }
    }

    /**
     * 判断是否关闭
     * @return
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * 返回客户端配置
     * @return
     */
    protected RpcClientConfig getRpcClientConfig() {
        return rpcClientConfig;
    }

    /**
     * 创建channel
     */
    protected abstract void doOpen();

    /**
     * 执行远程连接
     */
    protected abstract void doConnect();

    /**
     * 执行断开连接
     */
    protected abstract void doDisConnect();

    /**
     * 执行关闭连接
     */
    protected abstract void doClose();
}
