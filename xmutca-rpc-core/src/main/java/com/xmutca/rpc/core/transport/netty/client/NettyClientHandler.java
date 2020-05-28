package com.xmutca.rpc.core.transport.netty.client;

import com.xmutca.rpc.core.common.Constants;
import com.xmutca.rpc.core.common.TransportType;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.rpc.RpcFuture;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.ClientPool;
import com.xmutca.rpc.core.transport.Transporter;
import com.xmutca.rpc.core.transport.netty.AbstractHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-01-02
 */
@Slf4j
@Sharable
public class NettyClientHandler extends AbstractHandler {

    /**
     * 上一次的交互时间
     */
    private long lastTime = System.currentTimeMillis();
    private final Client client;
    private final RpcClientConfig rpcClientConfig;
    private final InetSocketAddress remoteAddress;

    public NettyClientHandler(Client client, RpcClientConfig rpcClientConfig, InetSocketAddress remoteAddress) {
        this.client = client;
        this.remoteAddress = remoteAddress;
        this.rpcClientConfig = rpcClientConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Transporter msg) {
        lastTime = System.currentTimeMillis();
        RpcFuture.received(msg.getRequestId(), (RpcResponse) msg.getBody());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("netty client caught exception", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientPool.addClient(rpcClientConfig.getMetadata(), remoteAddress, client);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ClientPool.removeClient(rpcClientConfig.getMetadata(), remoteAddress, client);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 不需要往下执行
        if (!checkInstanceofIdleStateEvent(evt)) {
            super.userEventTriggered(ctx, evt);
            return;
        }

        // 判断上一次接收消息的时间，或者是上一次心跳的时间是否已经超过阈值，如果是则进行心跳报文的发送
        IdleState state = ((IdleStateEvent) evt).state();
        if (state == IdleState.WRITER_IDLE && isOverTime(lastTime, Constants.DEFAULT_WRITER_IDLE_TIME_SECONDS)) {
            Transporter transporter = new Transporter();
            transporter.setType(TransportType.MODE_HEARTBEAT);
            ctx.writeAndFlush(transporter);
        }
    }
}
