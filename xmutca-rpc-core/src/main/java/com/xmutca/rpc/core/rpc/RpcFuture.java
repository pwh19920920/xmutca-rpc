package com.xmutca.rpc.core.rpc;

import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.exception.TimeoutException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-01-04
 */
public class RpcFuture {

    private final Long id;
    private final int timeout;
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    private volatile RpcResponse response;
    private static final Map<Long, RpcFuture> FUTURES = new ConcurrentHashMap<>();

    public RpcFuture(Long requestId, int timeout) {
        this.timeout = timeout != 0 ? timeout : 1000;
        this.id = requestId;
        FUTURES.put(id, this);
    }

    /**
     * 是否完成
     * @return
     */
    public boolean isDone() {
        return null != response;
    }

    /**
     * 响应处理
     * @param res
     */
    private void doReceived(RpcResponse res) {
        lock.lock();
        try {
            response = res;
            done.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 响应处理
     * @param requestId
     * @param response
     */
    public static void received(Long requestId, RpcResponse response) {
        RpcFuture future = removeFuture(requestId);
        if (future != null) {
            future.doReceived(response);
        }
    }

    /**
     * 获取数据，发送数据时使用
     * @return
     */
    public RpcResponse get() {
        return get(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取数据，发送数据时使用
     * @param timeout
     * @param unit
     * @return
     */
    public RpcResponse get(long timeout, TimeUnit unit) {
        if (!isDone()) {
            lock.lock();

            long start = System.currentTimeMillis();
            try {
                while (!isDone()) {
                    if (timeout <= 0) {
                        done.await();
                    } else {
                        done.await(timeout, unit);
                    }

                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (Exception ex) {
                throw new RpcException("execute ex", ex);
            } finally {
                lock.unlock();
            }

            if (!isDone()) {
                throw new TimeoutException("time out");
            }
        }
        return response;
    }

    /**
     * 移除
     * @param requestId
     * @return
     */
    public static RpcFuture removeFuture(Long requestId) {
        return FUTURES.remove(requestId);
    }
}
