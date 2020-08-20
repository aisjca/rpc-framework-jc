package rpc.framework.register;

import org.junit.jupiter.api.Test;
import rpc.framework.registry.ServiceDiscovery;
import rpc.framework.registry.ServiceRegistry;
import rpc.framework.registry.ZkServiceDiscovery;
import rpc.framework.registry.ZkServiceRegistry;

import java.net.InetSocketAddress;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/03 20:18
 */
public class ZkServiceRegisterTest {

    @Test
    void should_register_service_successful_and_lookup_service_by_service_name() throws Exception {
//        ServiceRegistry serviceRegistry = new ZkServiceRegistry();
//        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);
//        serviceRegistry.registerService("rpc.framework.registry.serviceRegistry", inetSocketAddress);
//        ServiceDiscovery serviceDiscovery = new ZkServiceDiscovery();
//        InetSocketAddress discoveryInetSocketAddress = serviceDiscovery.lookupService("rpc.framework.registry.serviceRegistry");
//        System.out.println(inetSocketAddress.equals(discoveryInetSocketAddress));

    }
}
