package com.xmutca.rpc.core.exception;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-30
 */
public class EnumException extends RuntimeException {

    public EnumException(String msg) {
        super(msg);
    }

    public EnumException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EnumException(Throwable cause) {
        super(cause);
    }
}
