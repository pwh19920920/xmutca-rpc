package com.xmutca.rpc.core.exception;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-31
 */
public class TimeoutException extends RpcException {

    public TimeoutException(String msg) {
        super(msg);
    }

    public TimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
