# rpc 服务端属性配置

# rpc服务所在的idc
rpc.app.idc=default

# rpc服务启动监听的端口
rpc.server.port=8899
#心跳间隔时间，单位：毫秒
rpc.heartbeat.gap=100

# 是否开启web服务治理（提供与普通rpc服务相同的能力，如服务注册，服务监听，服务路由）
rpc.web.enable=true

#是否启动tcp服务
rpc.enable.startup.tcp.server=true

# rpc service所在的包路径
rpc.service.base.package=${package}.${business}