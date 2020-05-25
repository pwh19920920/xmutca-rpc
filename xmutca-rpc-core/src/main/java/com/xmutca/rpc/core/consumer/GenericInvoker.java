package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.rpc.RpcRequest;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-08
 */
public interface GenericInvoker {

    /**
     * 泛化调用
     *
     * @param method         方法名，如：findPerson，如果有重载方法，需带上参数列表，如：findPerson(java.lang.String)
     * @param parameterTypes 参数类型
     * @param args           参数列表
     * @return 返回值
     */
    Object invoke(String method, String[] parameterTypes, Object[] args);

    /**
     * 泛化调用
     *
     * @param method         方法名，如：xxx.xxx.Test.findPerson，如果有重载方法，需带上参数列表，如：findPerson(java.lang.String)
     * @param parameterTypes 参数类型
     * @param args           参数列表
     * @return 返回值
     */
    Object invoke(String method, Class<?>[] parameterTypes, Object[] args);

    /**
     * 泛化调用
     * @param rpcRequest
     * @return
     */
    Object invoke(RpcRequest rpcRequest);
}
