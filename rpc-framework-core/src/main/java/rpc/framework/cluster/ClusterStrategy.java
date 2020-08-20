package rpc.framework.cluster;

import rpc.framework.utils.zookeeper.dto.ProviderService;

import java.util.List;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/03 20:40
 */
public interface ClusterStrategy {

    /**
     * 负载策略算法
     * @param providerServices
     * @return
     */
    public ProviderService select(List<ProviderService> providerServices);
}
