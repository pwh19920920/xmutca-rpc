package com.xmutca.rpc.core.serialize.impl.json;

import com.alibaba.fastjson.JSON;
import com.xmutca.rpc.core.serialize.Codec;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-28
 */
public class JsonCodec implements Codec {

    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<?> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
