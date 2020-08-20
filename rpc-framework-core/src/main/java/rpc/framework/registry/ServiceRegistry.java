package rpc.framework.registry;

import java.net.InetSocketAddress;

/**
 * @program rpc-framework-jc
 * @description:服务注册接口
 * @author: JC
 * @create: 2020/08/02 22:23
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     * @param service 服务实例对象
     * @param serviceClass 服务实例对象实现的接口类
     * @param inetSocketAddress  ip和端口
     * @param <T>
     * @throws Exception
     */
    public <T> void registerService(T service, Class<T> serviceClass, InetSocketAddress inetSocketAddress) throws Exception;

}
