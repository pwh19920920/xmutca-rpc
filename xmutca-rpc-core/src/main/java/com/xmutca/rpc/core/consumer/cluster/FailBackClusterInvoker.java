package com.xmutca.rpc.core.consumer.cluster;

import com.xmutca.rpc.core.common.NamedThreadFactory;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.rpc.invoke.Invoker;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 失败自动恢复
 * 失败自动恢复，后台记录失败请求，定时重发，通常用于消息通知操作。
 * **区别是会定时重发**
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-08
 */
@Slf4j
public class FailBackClusterInvoker extends AbstractClusterInvoker {

    private static final long RETRY_FAILED_PERIOD = 5 * 1000;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2, new NamedThreadFactory("failBack-cluster-timer", true));
    private final ConcurrentMap<RpcRequest, AbstractClusterInvoker> failed = new ConcurrentHashMap<>();
    private volatile ScheduledFuture<?> retryFuture;

    @Override
    protected RpcResponse doInvoke(RpcRequest rpcRequest, RpcClientConfig rpcClientConfig, ClientGroup groups) {
        Client client = select(groups);
        try {
            return client.send(rpcRequest);
        } catch (Exception e) {
            log.error("FailBack to invoke method {}, wait for retry in background. Ignored exception: {}", rpcRequest.getMethodName(), e.getMessage(), e);
            addFailed(rpcRequest, this);
        }
        return new RpcResponse();
    }

    /**
     * 添加到失败队列
     * @param rpcRequest
     * @param router
     */
    private void addFailed(RpcRequest rpcRequest, AbstractClusterInvoker router) {
        if (retryFuture == null) {
            synchronized (this) {
                if (retryFuture == null) {
                    retryFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
                        // 收集统计信息
                        try {
                            retryFailed();
                        } catch (Exception t) {
                            // 防御性容错
                            log.error("Unexpected error occur at collect statistic", t);
                        }
                    }, RETRY_FAILED_PERIOD, RETRY_FAILED_PERIOD, TimeUnit.MILLISECONDS);
                }
            }
        }
        failed.put(rpcRequest, router);
    }

    /**
     * 失败重试
     */
    private void retryFailed() {
        if (failed.size() == 0) {
            return;
        }
        for (Map.Entry<RpcRequest, AbstractClusterInvoker> entry : new HashMap<RpcRequest, AbstractClusterInvoker>(failed).entrySet()) {
            RpcRequest rpcRequest = entry.getKey();
            AbstractClusterInvoker invoker = entry.getValue();
            try {
                invoker.invoke(rpcRequest);
                failed.remove(rpcRequest);
            } catch (Exception e) {
                log.error("Failed retry to invoke method {}, waiting again.", rpcRequest.getMethodName(), e);
            }
        }
    }
}
