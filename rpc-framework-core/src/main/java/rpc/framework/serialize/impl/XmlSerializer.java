package rpc.framework.serialize.impl;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import rpc.framework.serialize.ISerializer;

/**
 * @program rpc-framework-jc
 * @description:序列化接口，所有序列化类都要实现这个接口
 * @author: JC
 * @create: 2020/07/31 15:32
 */
public class XmlSerializer implements ISerializer {

    private static final XStream xStream = new XStream(new DomDriver());


    @Override
    public <T> byte[] serialize(T obj) {
        return xStream.toXML(obj).getBytes();
    }


    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String xml = new String(data);
        return (T) xStream.fromXML(xml);
    }


}
