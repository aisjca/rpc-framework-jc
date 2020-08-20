package rpc.framework.cluster.engine;

import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.enumeration.ClusterStrategyEnum;
import rpc.framework.cluster.impl.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/03 21:04
 */
public class ClusterEngine {
    private static final Map<ClusterStrategyEnum, ClusterStrategy> clusterStrategyMap = new ConcurrentHashMap<>();
    static {
        clusterStrategyMap.put(ClusterStrategyEnum.Random, new RandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightRandom, new WeightRandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Polling, new PollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightPolling, new WeightPollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Hash, new HashClusterStrategyImpl());
    }

    public static ClusterStrategy queryClusterStrategy(String clusterStrategy) {
        ClusterStrategyEnum clusterStrategyEnum = ClusterStrategyEnum.queryByCode(clusterStrategy);
        if (clusterStrategyEnum == null) {
            //默认选择随机算法
            return new RandomClusterStrategyImpl();
        }
        return clusterStrategyMap.get(clusterStrategyEnum);
    }

}
