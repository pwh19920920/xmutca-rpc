package com.xmutca.rpc.core.transport.netty.server;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.transport.AbstractServer;
import com.xmutca.rpc.core.transport.netty.NettyDecoder;
import com.xmutca.rpc.core.transport.netty.NettyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
public class NettyServer extends AbstractServer {

    private static final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
    private static final NioEventLoopGroup workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyServerWorker", true));
    private ServerBootstrap bootstrap;

    @Override
    protected void doOpen() {
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new NettyDecoder(RpcRequest.class));
                        pipeline.addLast("encoder", new NettyEncoder());
                        pipeline.addLast("idleCheck", new IdleStateHandler(Constants.DEFAULT_READER_IDLE_TIME_SECONDS, 0, 0, TimeUnit.SECONDS));
                        pipeline.addLast("handler", new NettyServerHandler(getInvoker(), getThreadPoolExecutor()));
                    }
                });
    }

    /**
     * 执行绑定操作
     * @throws InterruptedException
     */
    @Override
    public void bind() throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.bind(getPort()).sync();
        Channel channel = channelFuture.channel();
        channel.closeFuture().sync();
    }

    @Override
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
