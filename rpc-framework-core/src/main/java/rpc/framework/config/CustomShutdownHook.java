package rpc.framework.config;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.utils.concurrent.ThreadPoolFactoryUtils;
import rpc.framework.utils.zookeeper.CuratorUtils;

import java.util.concurrent.ExecutorService;

/**
 * @program rpc-framework-jc
 * @description:当服务端（provider）关闭的时候做一些事情比如取消注册所有服务
 * @author: JC
 * @create: 2020/08/02 17:46
 */
@Slf4j
public class CustomShutdownHook {
    private final ExecutorService threadPool = ThreadPoolFactoryUtils.createDefaultThreadPool("custom-shutdown-hook-rpc-pool");
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            CuratorUtils.clearRegistry();
            threadPool.shutdown();
        }));

    }
}
