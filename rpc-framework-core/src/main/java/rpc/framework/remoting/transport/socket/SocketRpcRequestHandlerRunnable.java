package rpc.framework.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.factory.SingletonFactory;
import rpc.framework.handler.RpcRequestHandler;
import rpc.framework.remoting.dto.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/02 22:20
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {
    private Socket socket;
    private RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
        } catch (IOException |ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }
}
