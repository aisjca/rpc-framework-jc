package rpc.framework.serialize.engine;
import rpc.framework.serialize.ISerializer;
import rpc.framework.serialize.SerializeType;
import rpc.framework.serialize.impl.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/07/31 16:02
 */
public class SerializerEngine {
    public static final Map<SerializeType, ISerializer> serializerMap = new ConcurrentHashMap<>();
    static{
        serializerMap.put(SerializeType.DefaultJavaSerializer, new DefaultJavaSerializer());
        serializerMap.put(SerializeType.HessianSerializer, new HessianSerializer());
        serializerMap.put(SerializeType.XmlSerializer, new XmlSerializer());
        serializerMap.put(SerializeType.MarshallingSerializer, new MarshallingSerializer());
        serializerMap.put(SerializeType.KryoSerializer, new KryoSerializer());
    }

    public static ISerializer getSerializerImpl(SerializeType serializeType) {
        return serializerMap.get(serializeType);
    }

    public static <T> byte[] serialize(T obj, String serializeType) {
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null) {
            throw new RuntimeException("serialize is null");
        }
        ISerializer serializer = serializerMap.get(serialize);
        if (serializer == null) {
            throw new RuntimeException("serialize error");
        }
        try{
            return serializer.serialize(obj);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz, String serializeType) {

        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null) {
            throw new RuntimeException("serialize is null");
        }
        ISerializer serializer = serializerMap.get(serialize);
        if (serializer == null) {
            throw new RuntimeException("serialize error");
        }

        try {
            return serializer.deserialize(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
