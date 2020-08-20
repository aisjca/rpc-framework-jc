package rpc.framework.exception;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/02 17:37
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        super(message);
    }
}
