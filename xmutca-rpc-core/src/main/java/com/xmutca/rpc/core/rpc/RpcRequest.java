package com.xmutca.rpc.core.rpc;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.common.TransportType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Rpc请求体
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
@Setter
@Getter
public class RpcRequest {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] arguments;

    /**
     * 附加参数
     */
    private Map<String, String> attachments;

    /**
     * 传输类型
     */
    private TransportType transportType;

    /**
     * 设置附加参数
     * @param key
     * @param value
     */
    public void setAttachment(String key, String value) {
        if (attachments == null) {
            attachments = new HashMap<>(16);
        }
        attachments.put(key, value);
    }

    public String getAttachment(String key) {
        if (attachments == null) {
            return null;
        }
        return attachments.get(key);
    }

    public String getAttachment(String key, String defaultValue) {
        if (attachments == null) {
            return defaultValue;
        }

        String value = attachments.get(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 获取fullName
     * @return
     */
    public String getFullName() {
        return String.format("%s.%s", getClassName(), getMethodName());
    }

    /**
     * build类
     */
    public static final class RpcRequestBuilder {
        private String className;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] arguments;
        private Map<String, String> attachments;
        private TransportType transportType;

        private RpcRequestBuilder() {
        }

        public static RpcRequestBuilder rpcRequest() {
            return new RpcRequestBuilder();
        }

        public RpcRequestBuilder className(String className) {
            this.className = className;
            return this;
        }

        public RpcRequestBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public RpcRequestBuilder parameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public RpcRequestBuilder arguments(Object[] arguments) {
            this.arguments = arguments;
            return this;
        }

        public RpcRequestBuilder attachments(Map<String, String> attachments) {
            this.attachments = attachments;
            return this;
        }

        public RpcRequestBuilder transportType(TransportType transportType) {
            this.transportType = transportType;
            return this;
        }

        public RpcRequest build() {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setClassName(className);
            rpcRequest.setMethodName(methodName);
            rpcRequest.setParameterTypes(parameterTypes);
            rpcRequest.setArguments(arguments);
            rpcRequest.setAttachments(attachments);
            rpcRequest.setTransportType(transportType);
            return rpcRequest;
        }
    }
}