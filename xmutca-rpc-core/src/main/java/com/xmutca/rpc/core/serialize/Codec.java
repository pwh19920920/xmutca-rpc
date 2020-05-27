package com.xmutca.rpc.core.serialize;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
public interface Codec {

    /**
     * 加密
     * @param obj
     * @return
     */
    byte[] encode(Object obj);

    /**
     * 解密
     * @param <T>
     * @param bytes
     * @param clazz
     * @return
     */
    <T> Object decode(byte[] bytes, Class<T> clazz);
}
