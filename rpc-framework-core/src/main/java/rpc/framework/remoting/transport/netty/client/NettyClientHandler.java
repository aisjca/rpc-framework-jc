package rpc.framework.remoting.transport.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import rpc.framework.exception.RpcException;
import rpc.framework.factory.SingletonFactory;
import rpc.framework.remoting.dto.RpcResponse;

/**
 * @program rpc-framework-jc
 * @description:自定义客户端 ChannelHandler 来处理服务端发过来的数据
 * @author: JC
 * @create: 2020/08/03 15:30
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler {
    private final UnprocessedRequests unprocessedRequests;
    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 读取服务端传输的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("client receive msg: [{}]", msg);
            RpcResponse rpcResponse = (RpcResponse) msg;
            unprocessedRequests.complete(rpcResponse);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 处理客户端消息发生异常的时候被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
