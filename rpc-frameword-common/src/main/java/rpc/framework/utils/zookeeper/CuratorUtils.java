package rpc.framework.utils.zookeeper;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import rpc.framework.exception.RpcException;
import rpc.framework.utils.helper.IPHelper;
import rpc.framework.utils.zookeeper.dto.ProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/02 20:14
 */
@Slf4j
public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 5;
    private static final String CONNECT_STRING = "127.0.0.1:2181";
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    // 某个节点下，对于的子节点的集合的缓存
    private static Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    //服务提供者列表,Key:服务提供者接口  value:服务提供者服务方法列表
    private static Map<String, List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();
    private static Set<String> registeredPathSet = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;

    static {
        zkClient=getZkClient();
    }

    private CuratorUtils() {
    }

    public static void registerProvider(List<ProviderService> serviceMetaData) throws Exception {
        if (CollectionUtils.isEmpty(serviceMetaData)) {
            log.error("没有服务可以注册");
            return;
        }
        for (ProviderService provider : serviceMetaData) {
            String serviceItfKey = provider.getServiceItf().getName();
            List<ProviderService> providers = providerServiceMap.get(serviceItfKey);
            if (providers == null) {
                providers = new ArrayList();
            }
            providers.add(provider);
            providerServiceMap.put(serviceItfKey, providers);
        }
        String APP_KEY = serviceMetaData.get(0).getAppKey();
        String ZK_PATH = ZK_REGISTER_ROOT_PATH + "/" + APP_KEY;
        if (zkClient.checkExists().forPath(ZK_PATH) == null) {
            createPersistentNode(ZK_PATH);
        }
        for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
            // 服务分组
            String groupName = entry.getValue().get(0).getGroupName();
            //创建服务提供者
            String serviceNode = entry.getKey();
            //服务注册到zookeeper的注册地址
            String servicePath = ZK_PATH + "/" + groupName + "/" + serviceNode;
            if (zkClient.checkExists().forPath(servicePath) == null) {
                createPersistentNode(servicePath);
            }
            //创建当前服务器节点
            int serverPort = entry.getValue().get(0).getServerPort();//服务端口
            int weight = entry.getValue().get(0).getWeight();//服务权重
            int workerThreads = entry.getValue().get(0).getWorkerThreads();//服务工作线程
            String localIp = entry.getValue().get(0).getServerIp();
            String currentServiceIpNode = servicePath + localIp + "|" + serverPort + "|" + weight + "|" + workerThreads + "|" + groupName;
            if (zkClient.checkExists().forPath(currentServiceIpNode) == null) {
                //注意,这里创建的是临时节点
                createEPHEMERALNode(currentServiceIpNode);
            }
            //TODO 监听
            registerWatcher(zkClient,servicePath);
        }
    }
    /**
     * @description: 创建临时节点，临时节点会因为客户端断开连接而被删除
     * @Param path 节点路径
     * @date: 2020/8/2
     */
    public static void createEPHEMERALNode(String path) {
        try{
            if (registeredPathSet.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("节点已经存在，节点为:[{}]", path);
            } else {
                // node path:/my-rpc/rpc.framework.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                log.info("节点创建成功，节点为:[{}]", path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * @description: 创建持久化节点。不同于临时节点，持久化节点不会因为客户端断开连接而被删除
     * @Param path 节点路径
     * @date: 2020/8/2
     */
    public static void createPersistentNode(String path) {
        try{
            if (registeredPathSet.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("节点已经存在，节点为:[{}]", path);
            } else {
                // node path:/my-rpc/rpc.framework.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功，节点为:[{}]", path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * client从ZK获取服务提供者列表
     * @param remoteAppKey
     * @param groupName
     * @return
     * @throws Exception
     */
    public static Map<String, List<ProviderService>> fetchOrUpdateServiceMetaData(String remoteAppKey, String groupName) throws Exception {
        Map<String, List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();

        //从ZK获取服务提供者列表
        String providePath = ZK_REGISTER_ROOT_PATH + "/" + remoteAppKey + "/" + groupName;
        List<String> providerServices = getChildrenNodes(providePath);

        for (String serviceName : providerServices) {
            String servicePath = providePath + "/" + serviceName ;
            List<String> ipPathList = getChildrenNodes(servicePath);
            for (String ipPath : ipPathList) {
                String serverIp = StringUtils.split(ipPath, "|")[0];
                String serverPort = StringUtils.split(ipPath, "|")[1];
                int weight = Integer.parseInt(StringUtils.split(ipPath, "|")[2]);
                int workerThreads = Integer.parseInt(StringUtils.split(ipPath, "|")[3]);
                String group = StringUtils.split(ipPath, "|")[4];

                List<ProviderService> providerServiceList = providerServiceMap.get(serviceName);
                if (providerServiceList == null) {
                    providerServiceList = Lists.newArrayList();
                }
                ProviderService providerService = new ProviderService();

                try {
                    providerService.setServiceItf(ClassUtils.getClass(serviceName));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                providerService.setServerIp(serverIp);
                providerService.setServerPort(Integer.parseInt(serverPort));
                providerService.setWeight(weight);
                providerService.setWorkerThreads(workerThreads);
                providerService.setGroupName(group);
                providerServiceList.add(providerService);

                providerServiceMap.put(serviceName, providerServiceList);
            }

        }
        return providerServiceMap;
    }
    /**
     * @description: 根据path来获取某个地址下的子节点。
     * @Param path 节点地址
     * @return: java.util.List<java.lang.String>
     * @author: JC
     * @date: 2020/8/2
     */
    public static List<String> getChildrenNodes(String path) {
        if (serviceAddressMap.containsKey(path)) {
            return serviceAddressMap.get(path);
        }
        List<String> result;
        try {
            result = zkClient.getChildren().forPath(path);
            serviceAddressMap.put(path, result);
            registerWatcher(zkClient, path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
        return result;
    }

    /**
     * @description: 注册监听指定节点。
     * @date: 2020/8/2
     */
    private static void registerWatcher(CuratorFramework zkClient, String path) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);//监听path路径下的所有节点
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework,pathChildrenCacheEvent)->{
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
            serviceAddressMap.put(path, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * @description:清空注册中心的数据
     * @date: 2020/8/2
     */
    public static void clearRegistry() {
        registeredPathSet.stream().parallel().forEach(p->{
            try {
                zkClient.delete().forPath(p);
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e.getCause());
            }
        });
        log.info("服务端（Provider）所有注册的服务都被清空:[{}]", registeredPathSet.toString());
    }
    private static CuratorFramework getZkClient() {
        // 重试策略。重试5次，并且会增加重试之间的睡眠时间。
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                //要连接的服务器(可以是服务器列表)
                .connectString(CONNECT_STRING)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

}
