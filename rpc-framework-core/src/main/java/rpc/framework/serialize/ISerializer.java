package rpc.framework.serialize;

/**
 * @program rpc-framework-jc
 * @description:序列化接口，所有序列化类都要实现这个接口
 * @author: JC
 * @create: 2020/07/31 15:32
 */
public interface ISerializer {

    /**
     * @description:序列化
     * @param obj 要序列化的对象
     * @return 字节数组
     * @author: JC
     * @date: 2020/7/31
     */
    public <T> byte[] serialize(T obj);


    /**
     * @description: 反序列化
     * @Param [bytes, clazz]
     * @return: 反序列化的对象
     * @author: JC
     * @date: 2020/7/31
     */
    public <T> T deserialize(byte[] bytes, Class<T> clazz);
}
