# rpc 客户端属性配置

# rpc 代理服务的ip和地址，相当于service mesh架构理念下的 sidecar
rpc.proxy.host=127.0.0.1
# rpc 代理服务的端口
rpc.proxy.port=8086

#服务端ip，当配置了该直接目标地址ip的时候，代理服务的配置无效，请求走直接路由（就是不需要通过proxy转发)
rpc.app.server.host=127.0.0.1
# 目标服务的端口
rpc.app.server.port=8899

#客户端连接池大小
rpc.client.connect.pool.size=10
#客户端连接默认的超时时间
rpc.client.connect.timeout=100
#客户端读默认的超时时间
rpc.client.socket.timeout=200