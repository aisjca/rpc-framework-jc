package rpc.framework.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import rpc.framework.factory.SingletonFactory;
import rpc.framework.registry.ServiceDiscovery;
import rpc.framework.registry.ZkServiceDiscovery;
import rpc.framework.remoting.dto.RpcRequest;
import rpc.framework.remoting.dto.RpcResponse;
import rpc.framework.remoting.transport.ClientTransport;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @program rpc-framework-jc
 * @description:基于 Netty 传输 RpcRequest。
 * @author: JC
 * @create: 2020/08/03 15:49
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;

    public NettyClientTransport() {
        serviceDiscovery = new ZkServiceDiscovery();
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRpcRequest(RpcRequest rpcRequest) throws Exception {
        // 构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        Channel channel = ChannelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            //放入未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            //向服务器发起请求，并启动监听
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: {}", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
