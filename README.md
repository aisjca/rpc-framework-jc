# 目录
* [项目介绍](https://github.com/aisjca/rpc-framework-jc#项目介绍)
* [执行步骤](https://github.com/aisjca/rpc-framework-jc#执行步骤)
* [rpc-framework-common](https://github.com/aisjca/rpc-framework-jc#rpc-framework-common)
* [rpc-framework-core](https://github.com/aisjca/rpc-framework-jc#rpc-framework-core)
* [rpc-framework-service](https://github.com/aisjca/rpc-framework-jc#rpc-framework-service)



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

# rpc-framework-common

### 简介

实现要调用的公共方法

### 相关包的介绍

#### config

##### ClientConfig类

从配置文件读取客户相关信息

##### ServerConfig类

从配置文件读取服务相关信息

#### enumeration

##### ClusterStrategyEnum类

负载均衡相关算法枚举类

##### RpcErrorMessageEnum类

Rpc服务错误信息的枚举类

##### RpcResponseCode类

Rpc服务调用相关错误码枚举类

#### exception

##### RpcException类

Rpc服务自定义RuntimeException

##### SerializeException类

序列化自定义RuntimeException

#### factory

##### SingletonFactory

获取单例对象的工厂类

#### utils

##### ThreadPoolFactoryUtils类

ThreadPool(线程池) 的工具类.

##### IPHelper类

通过获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。获得符合条件的一个IpV4地址

##### InvokerService类

服务注册中心的服务消费者注册信息

##### ProviderService 类

服务注册中心的服务提供者注册信息

##### CuratorUtils类

实现 Zookeeper相关方法，如节点注册，获取服务提供者列表，查找path地址下的子节点，清空注册中心的数据。

# rpc-framework-core

### 简介

实现Netty和zookeeper核心方法

### 相关包的介绍

#### cluster

具体实现各个负载均衡的算法

#### config

#####  CustomShutdownHook类

当服务端关闭的时候取消注册所有服务

##### PropertyConfig类

初始化zookeeper相关配置值

#### handler

##### RpcRequestHandler类

RpcRequest 的处理器

#### provider

##### ServiceProvider类

保存和提供服务实例对象。

#### proxy

##### RpcClientProxy类

实现动态代理方法

#### register

实现zookeeper的服务注册和发现

#### remoting

实现Netty的连接，释放，传输

#### serialize

实现序列化方法

# rpc-framework-service

### 简介

保存要注册到zookeeper的服务

