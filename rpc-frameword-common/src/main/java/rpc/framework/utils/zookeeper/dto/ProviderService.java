package rpc.framework.utils.zookeeper.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @program rpc-framework-jc
 * @description:服务注册中心的服务提供者注册信息
 * @author: JC
 * @create: 2020/08/03 20:46
 */
@Data
public class ProviderService implements Serializable {
    //服务接口
    private Class<?> serviceItf;
    //服务对象实例
    private transient Object serviceObject;
    //服务方法
    @JsonIgnore
    private transient Method serviceMethod;
    //服务ip
    private String serverIp;
    //服务端口
    private int serverPort;
    private long timeout;
    //该服务提供者权重
    private int weight;
    //服务端线程数
    private int workerThreads;
    //服务提供者唯一标识
    private String appKey;
    //服务分组组名
    private String groupName;

    public ProviderService copy() {
        ProviderService providerService = new ProviderService();
        providerService.setServiceItf(serviceItf);
        providerService.setServiceObject(serviceObject);
        providerService.setServiceMethod(serviceMethod);
        providerService.setServerIp(serverIp);
        providerService.setServerPort(serverPort);
        providerService.setTimeout(timeout);
        providerService.setWeight(weight);
        providerService.setWorkerThreads(workerThreads);
        providerService.setAppKey(appKey);
        providerService.setGroupName(groupName);
        return providerService;
    }

}
