package rpc.framework.cluster.impl;

import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.utils.zookeeper.dto.ProviderService;
import rpc.framework.utils.helper.IPHelper;

import java.util.List;

/**
 * @program rpc-framework-jc
 * @description:软负载哈希算法实现
 * @author: JC
 * @create: 2020/08/03 21:06
 */
public class HashClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //获取调用方ip
        String localIP = IPHelper.localIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerServices.size();

        return providerServices.get(hashCode % size);
    }
}
