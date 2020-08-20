package rpc.framework.registry;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.config.ServerConfig;
import rpc.framework.utils.zookeeper.dto.ProviderService;
import rpc.framework.utils.helper.IPHelper;
import rpc.framework.utils.zookeeper.CuratorUtils;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @program rpc-framework-jc
 * @description: 实现服务注册
 * @author: JC
 * @create: 2020/08/02 22:27
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    //服务超时时间
    private static long timeout;
    //服务提供者唯一标识
    private static String appKey;
    //服务分组组名
    private static String groupName = "default";
    //服务提供者权重,默认为1 ,范围为[1-100]
    private static int weight = 1;
    //服务端线程数,默认10个线程
    private static int workerThreads = 10;


    /**
     *
     * @param service 服务实例对象
     * @param serviceClass 服务实例对象实现的接口类
     * @param inetSocketAddress  ip和端口
     * @param <T>
     * @throws Exception
     */
    @Override
    public <T> void registerService(T service, Class<T> serviceClass, InetSocketAddress inetSocketAddress) throws Exception {
        //获取要注册的服务列表
        List<ProviderService> providerServicesList = buildProviderServiceInfos(service,serviceClass, inetSocketAddress);
        //注册服务列表
        CuratorUtils.registerProvider(providerServicesList);

    }

    private static <T> List<ProviderService> buildProviderServiceInfos(T service, Class<T> serviceClass, InetSocketAddress inetSocketAddress) {
        List<ProviderService> providerList = new ArrayList<>();
        Method[] methods = service.getClass().getDeclaredMethods();
        String host = inetSocketAddress.toString().split(":")[0];//服务地址
        String serverPort=inetSocketAddress.toString().split(":")[1];//服务端口
        //从配置文件获取服务相关信息
        weight = ServerConfig.getWeight();
        workerThreads = ServerConfig.getWorkerThreads();
        appKey = ServerConfig.getAppKey();
        groupName = ServerConfig.getGroupName();
        timeout = ServerConfig.getTimeout();
        for (Method method : methods) {
            ProviderService providerService = new ProviderService();
            providerService.setServiceItf(serviceClass);
            providerService.setServiceObject(service);
            providerService.setServerIp(host);
            providerService.setServerPort(Integer.parseInt(serverPort));
            providerService.setTimeout(timeout);
            providerService.setServiceMethod(method);
            providerService.setWeight(weight);
            providerService.setWorkerThreads(workerThreads);
            providerService.setAppKey(appKey);
            providerService.setGroupName(groupName);
            providerList.add(providerService);
        }
        return providerList;
    }


}
