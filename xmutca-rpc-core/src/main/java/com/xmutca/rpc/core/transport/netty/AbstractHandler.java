package com.xmutca.rpc.core.transport.netty;

import com.xmutca.rpc.core.transport.Transporter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-02
 */
public abstract class AbstractHandler extends SimpleChannelInboundHandler<Transporter> {

    /**
     * 检查是否是IdleStateEvent的实例
     * @param evt
     * @return
     */
    public boolean checkInstanceofIdleStateEvent(Object evt) {
        return evt instanceof IdleStateEvent;
    }

    /**
     * 检查最后一次时间是否超过时间
     * @param lastTime
     * @param timeout
     * @return
     */
    public boolean isOverTime(long lastTime, int timeout) {
        return System.currentTimeMillis() - lastTime > timeout * 1000;
    }
}
