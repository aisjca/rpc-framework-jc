package rpc.framework.serialize.impl;


import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import rpc.framework.serialize.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @program rpc-framework-jc
 * @description:序列化接口，所有序列化类都要实现这个接口
 * @author: JC
 * @create: 2020/07/31 15:32
 */
public class HessianSerializer implements ISerializer {


    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (data == null) {
            throw new NullPointerException();
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(is);
            return (T) hi.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
