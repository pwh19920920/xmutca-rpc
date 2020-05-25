package com.xmutca.rpc.core.transport.netty;

import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.transport.TransportChannel;
import com.xmutca.rpc.core.transport.Transporter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.Future;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-01
 */
public class NettyChannel implements TransportChannel {

    private Channel channel;

    public NettyChannel(Channel ch) {
        this.channel = ch;
    }

    @Override
    public void send(Transporter transporter) {
        if (!isConnected()) {
            throw new RpcException("Failed to send message, cause: Channel closed.");
        }

        Future<ChannelFuture> future = channel.eventLoop().submit(() -> channel.writeAndFlush(transporter));
        try {
            ChannelFuture channelFuture = future.get();
            Throwable cause = channelFuture.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable throwable) {
            throw new RpcException("Failed to send message" + transporter, throwable);
        }
    }

    @Override
    public boolean isConnected() {
        if (null == channel) {
            return false;
        }
        return channel.isActive();
    }

    @Override
    public void close() {
        if (isConnected()) {
            channel.close();
        }
    }
}
