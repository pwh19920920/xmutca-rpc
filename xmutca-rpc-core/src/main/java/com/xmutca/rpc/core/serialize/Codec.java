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
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T decode(byte[] bytes, Class<?> clazz);
}
