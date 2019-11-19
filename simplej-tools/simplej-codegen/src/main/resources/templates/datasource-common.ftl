# 数据库公共配置
url=jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull
#最小初始化连接数,请根据实际需求调整大小
initialSize=5
#最小空闲连接,请根据实际需求调整大小
minIdle=5
#最大空闲连接,请根据实际需求调整大小
maxIdle=10
#最大活跃数,请根据实际需求调整大小
maxActive=30
#拿连接等待的最大时间,请根据实际需求调整大小
maxWait=6000
#每xxx秒运行一次空闲连接回收器,请根据实际需求调整大小
timeBetweenEvictionRunsMillis=90000
#池中的连接空闲xxx分钟后被回收,请根据实际需求调整大小
minEvictableIdleTimeMillis=1800000
#借出连接时不要测试，否则很影响性能
testOnBorrow=false
#归还连接时不要测试，否则很影响性能
testOnReturn=false
#指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除.
testWhileIdle=true
#连接池类型DRUID,TOMCAT_JDBC,Hikari,BoneCP, DBCP2;不区分大小写
dataSourceType=DRUID
#测试查询sql
validationQuery = SELECT 1

#分库查询线程池配置
#Core pool size is the minimum number of workers to keep alive
db.threadpool.corePoolSize=3
#Maximum pool size.
db.threadpool.maximumPoolSize=128
#idle thread keepalive time ,unit is seconds
db.threadpool.keepAliveTime=120
#workqueue length
db.threadpool.workQueue.capacity=10000
#并行查询子任务超时时间
db.thread.query.timeout=20000
