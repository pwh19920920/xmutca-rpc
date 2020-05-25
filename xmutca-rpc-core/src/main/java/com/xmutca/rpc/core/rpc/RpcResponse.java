package com.xmutca.rpc.core.rpc;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.exception.RpcException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Rpc返回体
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
@Getter
@Setter
public class RpcResponse implements Serializable {

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 异常信息
     */
    private RpcException exception;

    /**
     * 附加参数
     */
    private Map<String, String> attachments = new HashMap<>();

    /**
     * 是否存在异常
     * @return
     */
    public boolean hasException() {
        return null != exception;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
