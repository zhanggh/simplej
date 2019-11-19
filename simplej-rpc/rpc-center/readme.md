##说明
-rpc server和普通app的架构一致，只是稍微特殊的是它本身是注册中心，服务治理中心
-rpc server所在的机器上通用的需要部署rpc-proxy，也就是说所有服务包括rpc-server本身，都需要rpc-proxy协助进行网络中的通讯
