package rpc.framework.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import rpc.framework.config.CustomShutdownHook;
import rpc.framework.config.PropertyConfig;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.provider.ServiceProviderImpl;
import rpc.framework.registry.ServiceRegistry;
import rpc.framework.registry.ZkServiceRegistry;
import rpc.framework.remoting.dto.RpcRequest;
import rpc.framework.remoting.dto.RpcResponse;
import rpc.framework.remoting.transport.netty.code.NettyDecoder;
import rpc.framework.remoting.transport.netty.code.NettyEncoder;
import rpc.framework.serialize.ISerializer;
import rpc.framework.serialize.engine.SerializerEngine;

import java.net.InetSocketAddress;

/**
 * @program rpc-framework-jc
 * @description:服务端。接收客户端消息，并且根据客户端的消息调用相应的方法，然后返回结果给客户端
 * @author: JC
 * @create: 2020/08/03 10:48
 */
@Slf4j
public class NettyServer {
    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    private ISerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serializer = SerializerEngine.getSerializerImpl(PropertyConfig.getSerializeType());
        serviceProvider = new ServiceProviderImpl();
        serviceRegistry = new ZkServiceRegistry();
    }

    /**、
     *
     * @param service ServiceImpl
     * @param serviceClass  interface rpc.framework.service.HelloService
     * @param <T>
     * @throws Exception
     */
    public <T> void publishService(T service, Class<T> serviceClass) throws Exception {
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.registerService(service, serviceClass, new InetSocketAddress(host, port));
        start();
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyDecoder(serializer, RpcRequest.class));
                            ch.pipeline().addLast(new NettyEncoder(serializer, RpcResponse.class));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128);
            // 绑定端口，同步等待绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            //取消注册所有服务
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("occur exception when start server:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
