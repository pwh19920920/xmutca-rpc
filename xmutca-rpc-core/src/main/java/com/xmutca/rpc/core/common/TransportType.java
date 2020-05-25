package com.xmutca.rpc.core.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输类型
 * @author qudian
 */
public enum TransportType {

    /**
     * req - oneWay
     */
    MODE_REQ_ONE_WAY((byte)0x01),

    /**
     * req - twoWay
     */
    MODE_REQ_TWO_WAY((byte)0x02),

    /**
     * resp
     */
    MODE_RESP((byte)0x03),

    /**
     * heartbeat
     */
    MODE_HEARTBEAT((byte)0x04),
    ;

    private byte type;

    TransportType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    /**
     * map
     */
    private static final Map<Byte, TransportType> map = new ConcurrentHashMap<>();

    /**
     * 获取解码类型
     * @param type
     * @return
     */
    public static TransportType get(byte type) {
        if (!map.isEmpty()) {
            return map.get(type);
        }

        synchronized (map) {
            if (!map.isEmpty()) {
                return map.get(type);
            }

            for (TransportType transportType : TransportType.values()) {
                map.put(transportType.type, transportType);
            }
        }
        return map.get(type);
    }
}