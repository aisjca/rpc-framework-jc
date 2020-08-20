package rpc.framework.remoting.transport.netty.client;

import io.netty.channel.Channel;
import rpc.framework.factory.SingletonFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @program rpc-framework-jc
 * @description:用于获取 Channel 对象
 * @author: JC
 * @create: 2020/08/03 15:52
 */
public class ChannelProvider {
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();
    private static NettyClient nettyClient;
    static {
        nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    private  ChannelProvider() {
    }

    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // 判断是否有对应地址的连接
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            // 如果有的话，判断连接是否可用，可用的话就直接获取
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }
        //重新连接获取 Channel
        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channels.put(key, channel);
        return channel;
    }
}
