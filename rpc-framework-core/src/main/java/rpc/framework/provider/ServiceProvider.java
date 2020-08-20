package rpc.framework.provider;

/**
 * @program rpc-framework-jc
 * @description: 保存和提供服务实例对象。服务端使用。
 * @author: JC
 * @create: 2020/08/02 22:32
 */
public interface ServiceProvider {

    /**
     * @description: 保存服务实例对象和服务实例对象实现的接口类的对应关系
     * @Param service   服务实例对象
     * serviceClass 服务实例对象实现的接口类
     * T  服务接口的类型
     * @date: 2020/8/2
     */
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    /**
     * @description: 获取服务实例对象
     * @Param serviceName 服务实例对象实现的接口类的类名
     * return  服务实例对象
     * @date: 2020/8/2
     */
    Object getServiceProvider(String serviceName);
}
