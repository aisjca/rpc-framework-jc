package rpc.framework.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @program rpc-framework-jc
 * @description:获取单例对象的工厂类
 * @author: JC
 * @create: 2020/08/02 22:55
 */
public class SingletonFactory {
    private static Map<String, Object> objectMap = new HashMap<>();

    private SingletonFactory() {

    }

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance = objectMap.get(key);
        synchronized (c) {
            if (instance == null) {
                try {
                    instance = c.newInstance();
                    objectMap.put(key, instance);
                } catch (InstantiationException |IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return c.cast(instance);
    }
}
