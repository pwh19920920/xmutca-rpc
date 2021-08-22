package com.xmutca.rpc.core.exception;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-25
 */
public class ReferenceException extends RuntimeException {

    public ReferenceException(String msg) {
        super(msg);
    }

    public ReferenceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ReferenceException(Throwable cause) {
        super(cause);
    }
}
