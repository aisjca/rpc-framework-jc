package rpc.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program rpc-framework-jc
 * @description:服务注册相关信息初始化
 * @author: JC
 * @create: 2020/08/10 14:48
 */
public class ServerConfig {
    private static final Properties properties = new Properties();
    private static final String Server_CLASSPATH = "/server.properties";//读取配置文件路径

    private static String serviceItf; //服务接口
    private static int weight; //权重
    private static String groupName;//服务分组
    private static String AppKey;//注册服务的app名字
    private static int workerThreads;
    private static String serverPort;
    private static int timeout;
    /**
     * 初始化
     */
    static {
        InputStream is = null;
        try {
            is = ServerConfig.class.getResourceAsStream(Server_CLASSPATH);
            if (is == null) {
                throw new IllegalStateException("rpc.properties can not found in the classpath.");
            }
            properties.load(is);
            serviceItf=properties.getProperty("interface");
            AppKey = properties.getProperty("appKey");
            weight = Integer.parseInt(properties.getProperty("weight", "1"));
            groupName = properties.getProperty("groupName");
            workerThreads = Integer.parseInt(properties.getProperty("workerThreads","10"));
            serverPort = properties.getProperty("serverPort");
            timeout = Integer.parseInt(properties.getProperty("timeout", "600"));
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

    public static int getWeight() {
        return weight;
    }

    public static String getGroupName() {
        return groupName;
    }

    public static String getAppKey() {
        return AppKey;
    }

    public static int getWorkerThreads() {
        return workerThreads;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public static int getTimeout() {
        return timeout;
    }

    //Test
    public static void main(String[] args) {
        System.out.println(getAppKey()+" "+getGroupName()+" "+getServerPort()+" "+getServiceItf()+" "+getTimeout()+" "+getWeight()+" "+getWorkerThreads());
    }
}
