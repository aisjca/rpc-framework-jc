# 目录
* [项目介绍](https://github.com/aisjca/rpc-framework-jc#项目介绍)
* [执行步骤](https://github.com/aisjca/rpc-framework-jc#执行步骤)
* [](https://github.com/aisjca/rpc-framework-jc#)
* [](https://github.com/aisjca/rpc-framework-jc#)
* [](https://github.com/aisjca/rpc-framework-jc#)



# 项目介绍

该项目分别实现了网络传输、注册中心、序列化、动态代理，负载均衡的功能

主要关键内容是

1. 通过Netty基于Nio的方式实现了网络传输。
2. 使用Netty MessageToByteEncoder与ByteToMessageDecoder实现自定义数据传输协议的编辑码，并通过Kryo、 Marshalling、Hessian等5种序列化方法来实现网络传输的序列化。
3. 使用Zookeeper作为服务注册中心，并实现服务自动注册功能和服务发现功能。
4. 实现了软负载加权随机算法、软负载随机算法等5种算法来实现负载均衡策略。

# 执行步骤

1. 首先利用Docker打开 zookeeper
2. 运行rpc-framework-core/src/test/java/rpc/framework/server/文件下的NettyServerMain.java
3. 再运行rpc-framework-jc/rpc-framework-core/src/test/java/rpc/framework/client/文件下的NettyClientMain.java即可

# rpc-framework