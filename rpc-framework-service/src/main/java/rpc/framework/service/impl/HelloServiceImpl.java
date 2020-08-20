package rpc.framework.service.impl;

import lombok.extern.slf4j.Slf4j;
import rpc.framework.service.HelloService;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/04 23:26
 */
@Slf4j
public class HelloServiceImpl implements HelloService {


    @Override
    public String hello(String msg) {
        return "receive the msg is " + msg;
    }
}
