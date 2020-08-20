package rpc.framework.serialize;

import org.apache.commons.lang3.StringUtils;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/07/31 16:03
 */
public enum SerializeType {
    DefaultJavaSerializer("DefaultJavaSerializer"),
    HessianSerializer("HessianSerializer"),
    XmlSerializer("XmlSerializer"),
    MarshallingSerializer("MarshallingSerializer"),
    KryoSerializer("KryoSerializer"),
    ;
    private String serializeType;
    SerializeType(String serializeType) {
        this.serializeType = serializeType;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public static SerializeType queryByType(String serializeType) {
        if (StringUtils.isBlank(serializeType)) {
            return null;
        }
        for (SerializeType serialize : SerializeType.values()) {
            if (StringUtils.equals(serialize.getSerializeType(), serializeType)) {
                return serialize;
            }
        }
        return null;
    }
}
