package rpc.framework.remoting.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/02 17:13
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;

}
