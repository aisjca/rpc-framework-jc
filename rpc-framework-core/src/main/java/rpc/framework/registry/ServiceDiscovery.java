package rpc.framework.registry;

import java.net.InetSocketAddress;

/**
 * @program rpc-framework-jc
 * @description:服务发现接口
 * @author: JC
 * @create: 2020/08/02 22:22
 */
public interface ServiceDiscovery {
    /**
     * @description: 查找服务
     * @Param serviceName 服务名称
     * @date: 2020/8/2
     */
    InetSocketAddress lookupService(String serviceName) throws Exception;
}
