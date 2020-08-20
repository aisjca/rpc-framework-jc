package rpc.framework.server;

import rpc.framework.remoting.transport.netty.server.NettyServer;
import rpc.framework.service.HelloService;
import rpc.framework.service.impl.HelloServiceImpl;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/04 23:35
 */
public class NettyServerMain {
    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999);
        nettyServer.publishService(helloService,HelloService.class);
    }
}
