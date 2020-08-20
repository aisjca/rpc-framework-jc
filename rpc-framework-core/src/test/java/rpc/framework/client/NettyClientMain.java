package rpc.framework.client;

import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.remoting.transport.ClientTransport;
import rpc.framework.remoting.transport.netty.client.NettyClientTransport;
import rpc.framework.service.HelloService;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/04 23:37
 */
public class NettyClientMain {
    public static void main(String[] args) {
        ClientTransport rpcClient = new NettyClientTransport();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        for (int i = 0; i < 100; i++) {
            String hello = helloService.hello("hello，rpc " + i);
            System.out.println(hello);
        }
    }
}
