package rpc.framework.remoting.transport;

import rpc.framework.remoting.dto.RpcRequest;

/**
 * @program rpc-framework-jc
 * @description:传输 RpcRequest。
 * @author: JC
 * @create: 2020/08/02 17:43
 */
public interface ClientTransport {

    /**
     * @description:发送消息到服务端
     * @Param rpcRequest 消息体
     * @return: 服务端返回的数据
     * @date: 2020/8/2
     */
    Object sendRpcRequest(RpcRequest rpcRequest) throws Exception;
}
