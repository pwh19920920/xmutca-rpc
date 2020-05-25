package com.xmutca.rpc.core.transport.netty;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.serialize.Codec;
import com.xmutca.rpc.core.serialize.CodecType;
import com.xmutca.rpc.core.transport.Transporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.math.BigInteger;

/**
 * 编码实现
 * @author qudian
 */
public class NettyEncoder extends MessageToByteEncoder<Transporter> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Transporter msg, ByteBuf out) {
        // 基本参数信息
        out.writeShort(msg.getMagic());
        out.writeByte(msg.getType());
        out.writeByte(msg.getCodec());
        out.writeLong(msg.getRequestId());

        // 判空长度写0
        if (msg.getBody() == null) {
            out.writeInt(Constants.ZERO_LENGTH);
            return;
        }

        // 数据对象参数
        byte[] rawTarget = ExtensionLoader
                .getExtensionLoader(Codec.class)
                .getExtension(CodecType.getSimpleName(msg.getCodec()))
                .encode(msg.getBody());
        out.writeInt(rawTarget.length);
        out.writeBytes(rawTarget);
    }
}