package com.xmutca.rpc.core.serialize.impl.kryo;

import com.xmutca.rpc.core.serialize.Codec;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/27
 */
public class KryoCodec implements Codec {
    @Override
    public byte[] encode(Object obj) {
        return KryoUtils.serialize(obj);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return KryoUtils.deserialize(bytes, clazz);
    }
}
