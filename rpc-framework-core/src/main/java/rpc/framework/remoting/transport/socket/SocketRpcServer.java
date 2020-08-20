package rpc.framework.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.config.CustomShutdownHook;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.provider.ServiceProviderImpl;
import rpc.framework.registry.ServiceRegistry;
import rpc.framework.registry.ZkServiceRegistry;
import rpc.framework.utils.concurrent.ThreadPoolFactoryUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/02 22:21
 */
@Slf4j
public class SocketRpcServer {
    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer(String host, int port) {
        this.threadPool = ThreadPoolFactoryUtils.createDefaultThreadPool("socket-server-rpc-pool");
        this.host = host;
        this.port = port;
        this.serviceRegistry = new ZkServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    public <T> void publishService(T service, Class<T> serviceClass) throws Exception {
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.registerService(service,serviceClass, new InetSocketAddress(host, port));
        start();
    }

    private void start() {
        try(ServerSocket server=new ServerSocket()) {
            server.bind(new InetSocketAddress(host, port));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}
