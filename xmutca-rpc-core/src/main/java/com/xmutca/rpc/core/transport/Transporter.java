package com.xmutca.rpc.core.transport;

import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.serialize.CodecType;
import com.xmutca.rpc.core.common.TransportType;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 传输对象，传输协议 16位
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-28
 * ----------------------------------------------------------------------------------
 * |                        protocol                                                |
 * ----------------------------------------------------------------------------------
 * |  2byte | 1byte | 1byte     | 1byte     |   8byte   |   4byte    |              |
 * ----------------------------------------------------------------------------------
 * |        |       |           |           |           |            |              |
 * |  magic |  mode |serialize  | status    | requestId |  length    |   body       |
 * | 0xbabe |       |           |           |           |            |              |
 * ----------------------------------------------------------------------------------
 */
@Getter
@Setter
public class Transporter {

    private static final AtomicLong atomicLong = new AtomicLong();

    /**
     * 魔法数 2位
     */
    private short magic = (short) 0xbabe;

    /**
     * 请求模式 1位
     * 0x01 - req - oneWay
     * 0x02 - req - twoWay
     * 0x21 - resp
     * 0x22 - Heartbeat
     */
    private byte type;

    /**
     * 序列化方式 1位
     * 0x01 - JSON
     */
    private byte codec = CodecType.CODEC_TYPE_JSON.getType();

    /**
     * 请求id， 8位
     */
    private long requestId = atomicLong.getAndIncrement();

    /**
     * 请求长度，4位
     */
    private int length;

    /**
     * 内容体
     */
    private Object body;

    public void setType(TransportType transportType) {
        this.type = transportType.getType();
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setCodec(CodecType codec) {
        this.codec = codec.getType();
    }

    public void setCodec(byte codec) {
        this.codec = codec;
    }

    /**
     * 拷贝对象
     * @param rpcResponse
     * @return
     */
    public Transporter copyAndSet(RpcResponse rpcResponse) {
        Transporter transporter = new Transporter();
        transporter.setType(type);
        transporter.setCodec(codec);
        transporter.setRequestId(requestId);
        transporter.setLength(length);
        transporter.setBody(rpcResponse);
        return transporter;
    }
}
