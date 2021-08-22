package com.xmutca.rpc.core.rpc.invoke;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.common.InvokeHelper;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-01-02
 */
@Slf4j
public class RpcResponseInvoke implements Invoker<RpcRequest, RpcResponse> {

    private static final Map<String, Object> EXPORT_PROVIDER = new ConcurrentHashMap<>();

    /**
     * 导出提供者
     * @param className 类名
     * @param object 目标对象
     */
    public static void addProvider(String className, Object object) {
        EXPORT_PROVIDER.putIfAbsent(className, object);
    }

    /**
     * 执行具体方法
     * @param rpcRequest 请求对象
     * @return 返回结果
     */
    @Override
    public RpcResponse invoke(RpcRequest rpcRequest) {
        log.info("receive message: {}", JSON.toJSONString(rpcRequest));

        RpcResponse rpcResponse = new RpcResponse();
        Object serviceBean = EXPORT_PROVIDER.get(rpcRequest.getClassName());
        if (null == serviceBean) {
            rpcResponse.setException(new RpcException("provider service not found"));
            return rpcResponse;
        }

        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getArguments();

        try {
            Object result = InvokeHelper.invoke(serviceBean, methodName, parameterTypes, parameters);
            rpcResponse.setResult(result);
            return rpcResponse;
        } catch (Exception e) {
            rpcResponse.setException(new RpcException("provider execute ex", e));
            return rpcResponse;
        }
    }
}
