# 数据库配置,当我们需要配置多个数据库的时候，复制该文件，作为模板，输出多个数据源配置文件，相应地调整host,port，schema等信息即可
#数据库组名，如果不填，则归属到默认组：defaultGroup
groupName=defaultGroup
#数据库访问用户名
username=${userName}
#数据库访问密码
password=${password}
#数据库访问host
dbHost=${host}
#数据库访问端口
dbPort=${port}
#数据库名
schema=

#连接池类型DRUID,TOMCAT_JDBC,Hikari,BoneCP, DBCP2;不区分大小写
dataSourceType=DRUID
#数据库服务类型，master 或者 slaverXX 当我们需要读写分离的时候，写操作会在master服务，读操作落在slaver服务
isMaster=true
#当前文件下的所有配置是否生效
#数据库节点服务编号，可以认为的进行编码 ，只要保证同一个group下编号是唯一即可
serverNo=001
#是否为默认数据源
isDefaultDb=true
#当前文件下的所有配置是否生效 true 代表配置有效，false代表配置无效
#dbEnable=true
#每个数据源取一个唯一的id，默认会由系统生成一个唯一的id
dataSourceId=${dataSourceId}