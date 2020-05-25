package com.xmutca.rpc.core.common;

/**
 * 传输方式
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-17
 */
public enum TransporterType {

    /**
     * netty
     */
    TRANSPORTER_TYPE_NETTY("netty");

    private String name;

    TransporterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
