package com.xmutca.rpc.core.common;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-29
 */
public enum TransportStatus {

    STATUS_OK((byte)0x01);

    private byte type;

    TransportStatus(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}