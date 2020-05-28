package com.xmutca.rpc.core.exception;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/28
 */
public class RegistryException extends RuntimeException {

    public RegistryException(String msg) {
        super(msg);
    }

    public RegistryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RegistryException(Throwable cause) {
        super(cause);
    }
}
