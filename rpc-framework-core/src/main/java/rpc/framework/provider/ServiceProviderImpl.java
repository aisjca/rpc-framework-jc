package rpc.framework.provider;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.enumeration.RpcErrorMessageEnum;
import rpc.framework.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program rpc-framework-jc
 * @description:实现了 ServiceProvider 接口，可以将其看做是一个保存和提供服务实例对象的示例
 * @author: JC
 * @create: 2020/08/03 09:37
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * 接口名和服务的对应关系
     * note:处理一个接口被两个实现类实现的情况如何处理？（通过 group 分组）
     * key:service/interface name
     * value:service
     */
    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        log.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
