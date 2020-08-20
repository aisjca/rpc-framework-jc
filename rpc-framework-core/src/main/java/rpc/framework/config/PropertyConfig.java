package rpc.framework.config;

import rpc.framework.cluster.ClusterStrategy;
import rpc.framework.enumeration.ClusterStrategyEnum;
import rpc.framework.serialize.SerializeType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program rpc-framework-jc
 * @description: 获取相关配置值
 * @author: JC
 * @create: 2020/08/03 14:38
 */
public class PropertyConfig {
    private static final Properties properties = new Properties();
    private static final String PROPERTY_CLASSPATH = "/rpc.properties";
    //ZK服务地址
    private static String zkService = "";
    //ZK session超时时间
    private static int zkSessionTimeout;
    //ZK connection超时时间
    private static int zkConnectionTimeout;
    //序列化算法类型
    private static SerializeType serializeType;
    //负载均衡算法的枚举
    private static ClusterStrategyEnum clusterStrategyEnum;


    /**
     * 初始化
     */
    static {
        InputStream is = null;
        try {
            is = PropertyConfig.class.getResourceAsStream(PROPERTY_CLASSPATH);
            if (is == null) {
                throw new IllegalStateException("rpc.properties can not found in the classpath.");
            }
            properties.load(is);
            zkService = properties.getProperty("zk_service");
            zkSessionTimeout = Integer.parseInt(properties.getProperty("zk_sessionTimeout", "500"));
            zkConnectionTimeout = Integer.parseInt(properties.getProperty("zk_connectionTimeout", "500"));
            String seriType = properties.getProperty("serialize_type");
            serializeType = SerializeType.queryByType(seriType);
            if (serializeType == null) {
                throw new RuntimeException("serializeType is null");
            }
            clusterStrategyEnum = ClusterStrategyEnum.queryByCode(properties.getProperty("cluster_type"));
            if (clusterStrategyEnum == null) {
                throw new RuntimeException("clusterType is null");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getZkService() {
        return zkService;
    }

    public static int getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public static int getZkConnectionTimeout() {
        return zkConnectionTimeout;
    }

    public static SerializeType getSerializeType() {
        return serializeType;
    }

    public static ClusterStrategyEnum getClusterStrategyEnum() {
        return clusterStrategyEnum;
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getSerializeType() + " " + getZkService());
    }
}
