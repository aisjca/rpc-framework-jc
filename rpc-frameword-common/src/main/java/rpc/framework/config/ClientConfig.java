package rpc.framework.config;

import rpc.framework.enumeration.ClusterStrategyEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program rpc-framework-jc
 * @description:客户获取服务相关信息初始化
 * @author: JC
 * @create: 2020/08/10 14:48
 */
public class ClientConfig {
    private static final Properties properties = new Properties();
    private static final String Client_CLASSPATH = "/client.properties";//读取配置文件路径

    private static String serviceItf; //服务接口
    private static String clusterStrategy;//服务策略
    private static String remoteAppKey;//选择调用的app
    private static String groupName;//服务分组
    /**
     * 初始化
     */
    static {
        InputStream is = null;
        try {
            is = ClientConfig.class.getResourceAsStream(Client_CLASSPATH);
            if (is == null) {
                throw new IllegalStateException("rpc.properties can not found in the classpath.");
            }
            properties.load(is);
            serviceItf=properties.getProperty("interface");
            clusterStrategy = properties.getProperty("clusterStrategy");
            remoteAppKey = properties.getProperty("remoteAppKey");
            groupName = properties.getProperty("groupName");
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

    public static String getServiceItf() {
        return serviceItf;
    }

    public static String getClusterStrategy() {
        return clusterStrategy;
    }

    public static String getRemoteAppKey() {
        return remoteAppKey;
    }

    public static String getGroupName() {
        return groupName;
    }

    //Test
    public static void main(String[] args) {
        System.out.println(getClusterStrategy());
    }

}
