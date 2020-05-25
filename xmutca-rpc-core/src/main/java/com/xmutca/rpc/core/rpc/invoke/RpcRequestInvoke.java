package com.xmutca.rpc.core.rpc.invoke;

import com.xmutca.rpc.core.common.TransportType;
import com.xmutca.rpc.core.config.RpcClientConfig;
import com.xmutca.rpc.core.exception.RpcException;
import com.xmutca.rpc.core.rpc.RpcRequest;
import com.xmutca.rpc.core.rpc.RpcResponse;
import com.xmutca.rpc.core.rpc.RpcFuture;
import com.xmutca.rpc.core.transport.Client;
import com.xmutca.rpc.core.transport.Transporter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-01
 */
public class RpcRequestInvoke implements Invoker<RpcRequest, RpcResponse> {

    private Client client;

    private RpcClientConfig rpcClientConfig;

    public RpcRequestInvoke(RpcClientConfig rpcClientConfig, Client client) {
        this.rpcClientConfig = rpcClientConfig;
        this.client = client;
    }

    @Override
    public RpcResponse invoke(RpcRequest req) {
        // 判断通道状态
        if (!client.isConnected()) {
            throw new RpcException("channel is unActive, waiting for connect");
        }

        // 拼装数据
        Transporter transporter = new Transporter();
        transporter.setType(req.getTransportType() == null ? TransportType.MODE_REQ_TWO_WAY : req.getTransportType());
        transporter.setCodec(rpcClientConfig.getCodecType());
        transporter.setBody(req);

        // 异步判断, 如果未异步不关心返回结果
        if (TransportType.MODE_REQ_ONE_WAY == req.getTransportType()) {
            client.getChannel().send(transporter);
            return new RpcResponse();
        }

        // 发送请求
        int timeout = rpcClientConfig.getTimeout(req.getFullName());
        try {
            RpcFuture rpcFuture = new RpcFuture(transporter.getRequestId(), timeout);
            client.getChannel().send(transporter);
            return rpcFuture.get();
        } finally {
            // 避免未删除
            RpcFuture.removeFuture(transporter.getRequestId());
        }
    }
}
