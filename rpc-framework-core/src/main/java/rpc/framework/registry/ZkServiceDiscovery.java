package rpc.framework.registry;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.cluster.engine.ClusterEngine;
import rpc.framework.config.ClientConfig;
import rpc.framework.config.PropertyConfig;
import rpc.framework.utils.zookeeper.CuratorUtils;
import rpc.framework.utils.zookeeper.dto.InvokerService;
import rpc.framework.utils.zookeeper.dto.ProviderService;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program rpc-framework-jc
 * @description:  实现服务发现
 * @author: JC
 * @create: 2020/08/02 22:24
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {
    //选择的负载均衡策略
    private static ClusterStrategy clusterStrategy;
    //服务提供者列表,Key:服务提供者接口  value:服务提供者服务方法列表
    private static Map<String, List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();

    //client调用服务的Appkey
    private static String remoteAppKey = ClientConfig.getRemoteAppKey();
    //client调用服务的groupName
    private static String groupName = ClientConfig.getGroupName();
    @Override
    public InetSocketAddress lookupService(String serviceName) throws Exception {
        /**
         * 负载均衡
         */
        //获取负载均衡对应策略的实现
        clusterStrategy = ClusterEngine.queryClusterStrategy(PropertyConfig.getClusterStrategyEnum().getCode());
        //获取服务列表
        providerServiceMap = CuratorUtils.fetchOrUpdateServiceMetaData(remoteAppKey, groupName);
        List<ProviderService> providerServices = providerServiceMap.get(serviceName);
        ProviderService providerService = clusterStrategy.select(providerServices);
        log.info("成功找到服务地址:{},端口号为:{}", providerService.getServerIp(), providerService.getServerPort());
        return new InetSocketAddress(providerService.getServerIp(), providerService.getServerPort());

    }


}
