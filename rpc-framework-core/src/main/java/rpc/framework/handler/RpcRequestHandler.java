package rpc.framework.handler;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.enumeration.RpcResponseCode;
import rpc.framework.exception.RpcException;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.provider.ServiceProviderImpl;
import rpc.framework.remoting.dto.RpcRequest;
import rpc.framework.remoting.dto.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @program rpc-framework-jc
 * @description:RpcRequest 的处理器
 * @author: JC
 * @create: 2020/08/02 22:53
 */
@Slf4j
public class RpcRequestHandler {
    private static ServiceProvider serviceProvider = new ServiceProviderImpl();

    /**
     * @description:处理 rpcRequest ：调用对应的方法，然后返回方法执行结果
     * @date: 2020/8/3
     */
    public Object handle(RpcRequest rpcRequest) {
        //通过注册中心获取到目标类（客户端需要调用类）
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * @description: rpcRequest 和 service 对象特定的方法并返回结果
     * @Param rpcRequest 客户端请求
     *        service  提供服务的对象
     * @return: 目标方法执行的结果
     * @date: 2020/8/3
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            if (null == method) {
                return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
            }
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
