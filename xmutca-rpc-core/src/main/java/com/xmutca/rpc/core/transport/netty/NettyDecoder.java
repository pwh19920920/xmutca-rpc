package com.xmutca.rpc.core.transport.netty;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.common.ExtensionLoader;
import com.xmutca.rpc.core.serialize.Codec;
import com.xmutca.rpc.core.serialize.CodecType;
import com.xmutca.rpc.core.transport.Transporter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * @author qudian
 */
public class NettyDecoder extends ByteToMessageDecoder {

    private Class<?> decodeTarget;

    public NettyDecoder(Class<?> decodeTarget) {
        this.decodeTarget = decodeTarget;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Transporter transporter = new Transporter();
        transporter.setMagic(in.readShort());
        transporter.setType(in.readByte());
        transporter.setCodec(in.readByte());
        transporter.setRequestId(in.readLong());
        transporter.setLength(in.readInt());

        if (in.readableBytes() < transporter.getLength()) {
            in.resetReaderIndex();
            return;	// fix 1024k buffer splice limix
        }

        if (transporter.getLength() > Constants.ZERO_LENGTH) {
            // 读取数据
            byte[] rawTarget = new byte[transporter.getLength()];
            in.readBytes(rawTarget);

            // decode target
            Object target = ExtensionLoader
                    .getExtensionLoader(Codec.class)
                    .getExtension(CodecType.getSimpleName(transporter.getCodec()))
                    .decode(rawTarget, decodeTarget);
            transporter.setBody(target);
        }

        // return
        out.add(transporter);
    }
}

