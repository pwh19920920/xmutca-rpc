package com.xmutca.rpc.core.exception;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-25
 */
public class ClassNotFountException extends RuntimeException {

    public ClassNotFountException(String msg) {
        super(msg);
    }

    public ClassNotFountException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ClassNotFountException(Throwable cause) {
        super(cause);
    }
}
