package com.xmutca.rpc.core.transport.netty.client;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.AbstractClient;
import com.xmutca.rpc.core.transport.ClientPool;
import com.xmutca.rpc.core.transport.TransportChannel;
import com.xmutca.rpc.core.transport.netty.NettyChannel;
import com.xmutca.rpc.core.transport.netty.NettyDecoder;
import com.xmutca.rpc.core.transport.netty.NettyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
@Slf4j
public class NettyClient extends AbstractClient {

    private static final NioEventLoopGroup workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", true));
    private volatile Channel channel;
    private Bootstrap bootstrap;

    @Override
    protected void doOpen() {
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new NettyDecoder(RpcResponse.class));
                        pipeline.addLast("encoder", new NettyEncoder());
                        pipeline.addLast("idleCheck", new IdleStateHandler(0, Constants.DEFAULT_WRITER_IDLE_TIME_SECONDS, 0, TimeUnit.SECONDS));
                        pipeline.addLast("handler", new NettyClientHandler());
                    }
                });
    }

    /**
     * doOpen + doConnect
     */
    @Override
    protected void doConnect() {
        // connect
        ChannelFuture future = bootstrap.connect(getRemoteAddress());
        boolean ret = future.awaitUninterruptibly(Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            Channel newChannel = future.channel();
            try {
                // Close old channel
                // copy reference
                Channel oldChannel = channel;
                if (null != oldChannel) {
                    log.info("Close old netty channel on create new netty channel");
                    oldChannel.close();
                }
            } finally {
                // 如果client关闭
                if (isClosed()) {
                    try {
                        log.info("Close new netty channel, because the client closed.");
                        newChannel.close();
                    } finally {
                        channel = null;
                    }
                } else {
                    channel = newChannel;

                    // 添加到池子里
                    ClientPool.addClient(getRpcClientConfig().getMetadata(), getRemoteAddress(), this);
                }
            }
        } else if (future.cause() != null) {
            throw new RpcException("failed to connect to server, error message is " + future.cause().getMessage(), future.cause());
        } else {
            throw new RpcException("failed to connect to server, client-side timeout");
        }
    }

    @Override
    protected void doDisConnect() {
        log.info("doDisConnect");
    }

    @Override
    protected void doClose() {
        log.info("doClose");
        ClientPool.removeClient(getRpcClientConfig().getMetadata(), getRemoteAddress(), this);
    }

    /**
     * 获取通道
     *
     * @return
     */
    @Override
    public TransportChannel getChannel() {
        return new NettyChannel(channel);
    }
}
