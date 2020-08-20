package rpc.framework.cluster.impl;

import org.apache.commons.lang3.RandomUtils;
import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.utils.zookeeper.dto.ProviderService;

import java.util.List;

/**
 * @program rpc-framework-jc
 * @description:软负载随机算法实现
 * @author: JC
 * @create: 2020/08/03 21:12
 */
public class RandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        int MAX_LEN = providerServices.size();
        int index = RandomUtils.nextInt(0, MAX_LEN - 1);
        return providerServices.get(index);
    }
}
