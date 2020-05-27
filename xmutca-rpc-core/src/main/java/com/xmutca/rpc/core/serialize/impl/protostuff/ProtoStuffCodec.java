package com.xmutca.rpc.core.serialize.impl.protostuff;

import com.xmutca.rpc.core.serialize.Codec;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/27
 */
public class ProtoStuffCodec implements Codec {

    @Override
    public byte[] encode(Object obj) {
        return ProtoStuffUtils.serialize(obj);
    }

    @Override
    public <T> Object decode(byte[] bytes, Class<T> clazz) {
        return ProtoStuffUtils.deserialize(bytes, clazz);
    }
}
