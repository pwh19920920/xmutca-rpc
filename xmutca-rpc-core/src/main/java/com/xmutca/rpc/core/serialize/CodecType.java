package com.xmutca.rpc.core.serialize;

import com.xmutca.rpc.core.exception.EnumException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化类型
 * @author qudian
 */
public enum CodecType {

    /**
     * JSON序列化
     */
    CODEC_TYPE_JSON((byte)0x01, "json", "JSON序列化");

    private byte type;

    private String simpleName;

    private String desc;

    CodecType(byte type, String simpleName, String desc) {
        this.type = type;
        this.simpleName = simpleName;
        this.desc = desc;
    }

    public byte getType() {
        return type;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * map
     */
    private static final Map<Byte, CodecType> map = new ConcurrentHashMap<>();

    /**
     * 获取解码类型
     * @param type
     * @return
     */
    public static CodecType get(byte type) {
        if (!map.isEmpty()) {
            return map.get(type);
        }

        synchronized (map) {
            if (!map.isEmpty()) {
                return map.get(type);
            }

            for (CodecType codecType : CodecType.values()) {
                map.put(codecType.type, codecType);
            }
        }
        return map.get(type);
    }

    /**
     * 获取类名
     * @param type
     * @return
     */
    public static String getSimpleName(byte type) {
        CodecType codecType = get(type);
        if (null == codecType) {
            throw new EnumException(String.format("CodecType枚举信息不存在, code: %s", type));
        }

        return codecType.simpleName;
    }
}