package rpc.framework.remoting.transport.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import rpc.framework.exception.RpcException;
import rpc.framework.factory.SingletonFactory;
import rpc.framework.handler.RpcRequestHandler;
import rpc.framework.remoting.dto.RpcRequest;
import rpc.framework.remoting.dto.RpcResponse;
import rpc.framework.utils.concurrent.ThreadPoolFactoryUtils;

import java.util.concurrent.ExecutorService;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/03 12:11
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler {
    private static final String THREAD_NAME_PREFIX = "netty-server-handler-rpc-pool";
    private final RpcRequestHandler rpcRequestHandler;
    private final ExecutorService threadPool;

    public NettyServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        this.threadPool = ThreadPoolFactoryUtils.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPool.execute(()->{
            try {
                log.info("server receive msg: [{}] ", msg);
                RpcRequest rpcRequest = (RpcRequest) msg;
                //执行目标方法（客户端需要执行的方法）并且返回方法结果
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info(String.format("server get result: %s", result.toString()));
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    //返回方法执行结果给客户端
                    ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
                } else {
                    log.error("not writable now, message dropped");
                }
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e.getCause());
            }
        });
    }
}
