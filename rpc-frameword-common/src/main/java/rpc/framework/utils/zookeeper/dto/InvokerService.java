package rpc.framework.utils.zookeeper.dto;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/08 00:22
 */
@Data
public class InvokerService implements Serializable {
    private Class<?> serviceItf;
    private Object serviceObject;
    private Method serviceMethod;
    private String invokerIp;
    private int invokerPort;
    private long timeout;
    //服务提供者唯一标识
    private String remoteAppKey;
    //服务分组组名
    private String groupName = "default";

}
