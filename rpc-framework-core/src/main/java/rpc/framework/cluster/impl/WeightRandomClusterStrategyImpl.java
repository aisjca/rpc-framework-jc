package rpc.framework.cluster.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.utils.zookeeper.dto.ProviderService;

import java.util.List;

/**
 * @program rpc-framework-jc
 * @description:软负载加权随机算法实现
 * @author: JC
 * @create: 2020/08/03 21:13
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //存放加权后的服务提供者列表
        List<ProviderService> providerList = Lists.newArrayList();
        for (ProviderService provider : providerServices) {
            int weight = provider.getWeight();
            for (int i = 0; i < weight; i++) {
                providerList.add(provider.copy());
            }
        }

        int MAX_LEN = providerList.size();
        int index = RandomUtils.nextInt(0, MAX_LEN - 1);
        return providerList.get(index);
    }
}
