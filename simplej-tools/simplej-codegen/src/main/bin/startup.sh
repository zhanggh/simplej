#!/bin/bash

export LC_ALL=en_US.UTF-8
ulimit -s 20480
ulimit -c unlimited
export PATH=$PATH:/usr/sbin

PRG="$0"
PRGDIR=`dirname "$PRG"`
#current dir
BASEDIR=`cd "$PRGDIR" >/dev/null; pwd`

LOGDIR=${BASEDIR}/logs
STATUS_FILE=${PRGDIR}/status

echo "LOGDIR:${LOGDIR}"

JMX_PORT=8061
SERVER_API=simplej-codegen
START_TIME=20
TARGET_VERSION=1.8

#defalut value
PERM_SIZE=128
PERM_SIZE_MAX=256

#CMDB file to get domain name if DOMAIN_NAME is not specified
CMDB_DATA_INI=/apps/conf/cmdb/data.ini

USAGE()
{
  echo "usage: $0 start|stop|restart|status"
}

if [ $# -lt 1 ]; then
  USAGE
  exit -1
fi

CMD="$1"
shift

PID_FILE=${PRGDIR}/PID
ADDITIONAL_OPTS=$*;

if [[ "$RUN_ENVIRONMENT" = "dev" ]]; then
  ENVIRONMENT_MEM="-Xms512m -Xmx512m -Xss256K -XX:MaxDirectMemorySize=512m"
else
  ENVIRONMENT_MEM="-Xms2048m -Xmx2048m -XX:MaxDirectMemorySize=2048m"
fi

# define GC log path
if [ -d /dev/shm/ ]; then
	GC_LOG_FILE=/dev/shm/gc-simplej-codegen.log
else
	GC_LOG_FILE=${LOGDIR}/gc-simplej-codegen.log
fi

# set GC_THREADS
GC_THREADS="-XX:ParallelGCThreads=8"
if [[ -n "$PARALLEL_GC_THREADS" ]]; then
	GC_THREADS="-XX:ParallelGCThreads=${PARALLEL_GC_THREADS}"

	if [[ -n "$CONC_GC_THREADS" ]]; then
		GC_THREADS="$GC_THREADS -XX:ConcGCThreads=${CONC_GC_THREADS}"
	fi
fi

JAVA_OPTS="-Dsimplej-codegen.logfile=$LOGDIR/simplej-codegen -XX:+PrintCommandLineFlags -XX:-OmitStackTraceInFastThrow -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:AutoBoxCacheMax=20000 -XX:+PerfDisableSharedMem -Djava.security.egd=file:/dev/./urandom"
NETTY_OPTS="${CONNECTIONS} ${CHANNEL_IDL_TIME} -Dio.netty.leakDetectionLevel=disabled -Dio.netty.recycler.maxCapacity.default=0 -Dio.netty.allocator.numHeapArenas=20"
MEM_OPTS="-server ${ENVIRONMENT_MEM} -XX:NewRatio=1 -XX:MaxTenuringThreshold=2 -XX:+UseConcMarkSweepGC -XX:-CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=4096 -XX:+ParallelRefProcEnabled -XX:+ExplicitGCInvokesConcurrent -XX:+AlwaysPreTouch -XX:+PrintPromotionFailure ${GC_THREADS}"
GCLOG_OPTS="-Xloggc:${GC_LOG_FILE} -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -XX:+PrintGCDetails"
CRASH_OPTS="-XX:ErrorFile=${LOGDIR}/hs_err_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOGDIR}/"
JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dsun.rmi.transport.tcp.threadKeepAliveTime=75000 -Djava.rmi.server.hostname=127.0.0.1"
OTHER_OPTS="-Dstart.check.outfile=${STATUS_FILE} -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true"

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
JAVA_MINOR_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '_' '{print $2*1}')

# at least Java 1.7 required
if [[ "$JAVA_VERSION" < "1.7" ]]; then
    echo "Error: Unsupported the java version $JAVA_VERSION , please use the version $TARGET_VERSION and above."
    exit -1;
fi

if [[ "$JAVA_VERSION" < "1.8" ]]; then
  MEM_OPTS="$MEM_OPTS -XX:PermSize=${PERM_SIZE}m -XX:MaxPermSize=${PERM_SIZE_MAX}m -XX:ReservedCodeCacheSize=96M"
  if [ ${JAVA_MINOR_VERSION} -ge 79 ]; then
      JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCCause -XX:+CMSParallelInitialMarkEnabled"
  else
      JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationConcurrentTime"
  fi
else
  MEM_OPTS="$MEM_OPTS -XX:MetaspaceSize=${PERM_SIZE}m -XX:MaxMetaspaceSize=${PERM_SIZE_MAX}m -XX:ReservedCodeCacheSize=96M -XX:-TieredCompilation"
  if [ ${JAVA_MINOR_VERSION} -ge 11 ]; then
      JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelInitialMarkEnabled"
  fi
fi


BACKUP_GC_LOG()
{
 GCLOG_DIR=${LOGDIR}
 BACKUP_FILE="${GCLOG_DIR}/gc-simplej-codegen_$(date +'%Y%m%d_%H%M%S').log"

 if [ -f ${GC_LOG_FILE} ]; then
  echo "saving gc log ${GC_LOG_FILE} to ${BACKUP_FILE}"
  mv ${GC_LOG_FILE} ${BACKUP_FILE}

  if [[ $? != 0 ]]; then
    echo -e "\033[31mmove gc log ${GC_LOG_FILE} to ${BACKUP_FILE} failed! Exit.\033[0m"
    exit -1
  fi
 fi
}

GET_PID_BY_ALL_PORT()
{
  echo `lsof -n -P -i :${JMX_PORT} | grep LISTEN | awk '{print $2}' | head -n 1`
}


STOP()
{
  BACKUP_GC_LOG

  if [ -f $PID_FILE ] ; then
	PID=`cat $PID_FILE`
  else
    PID=$(GET_PID_BY_ALL_PORT)
  fi
  if [ "$PID" != "" ]
	then
	if [ -d /proc/$PID ];then
		LISTEN_STATUS=`cat ${STATUS_FILE}`
		echo "$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API stopping, ${LISTEN_STATUS}."
		kill $PID

		if [ x"$PID" != x ]; then
		  echo -e "$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API still running as process:$PID...\c"
		fi

		while  [ -d /proc/$PID ]; do
		  echo -e ".\c"
		  sleep 1
		done

		check_prot_time=0
        while check_prot
        do
	      ((check_prot_time++))
	      if ["$check_prot_time" -lt 5]; then
	        echo "waitting for prot release: $APP_LISTEN_PORT $APP_LISTEN_SSL_PORT $APP_LISTEN_HTTP_PORT $APP_LISTEN_HTTPS_PORT $JMX_PORT"
		    sleep 1
		  else
		    echo "ports released......"
	     fi
		done

		echo -e "\n$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API stop successfully"
		sleep 1
	else
		echo "$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API is not running."
	fi
  else
	echo "$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API is not running."
  fi
}

check_prot()
{
	netstat -tnl | grep "$JMX_PORT"
}

START()
{

  BACKUP_GC_LOG
  echo "" > ${STATUS_FILE}

  if [ -f $PID_FILE ] ; then
	PID=`cat $PID_FILE`
  fi
  if [ "$PID" != "" ]
	then
	if [ -d /proc/$PID ];then
	 echo -e "\033[31m  $SERVER_API is running, please stop it first!! \033[0m"
	 exit -1
	fi
  fi

  if [ ! -d "$LOGDIR" ]; then
    echo "Warning! The logdir: $LOGDIR not existed! Try to create the dir automatically."
    mkdir -p "$LOGDIR"
    if [ -d "$LOGDIR" ]; then
      echo "Create logdir: $LOGDIR successed!"
    else
      echo -e "\033[31m Create logdir: $LOGDIR failed, please check it! \033[0m"
      exit -1
    fi
  fi

  LISTEN_STATUS="osp port is ${APP_LISTEN_PORT}, osp ssl port is ${APP_LISTEN_SSL_PORT}, http port is ${APP_LISTEN_HTTP_PORT}, https port is ${APP_LISTEN_HTTPS_PORT}, JMX port is ${JMX_PORT}"
  echo "$SERVER_API starting, ${LISTEN_STATUS}."
  nohup java  $JAVA_OPTS $NETTY_OPTS $MEM_OPTS $GCLOG_OPTS $JMX_OPTS $CRASH_OPTS $OTHER_OPTS $ADDITIONAL_OPTS -jar $BASEDIR/simplej-codegen.jar >> $LOGDIR/simplej-codegen.out 2>&1 &
  PID=$!
  echo $PID > $PID_FILE

  sleep 3

# #903 "if[ ! -d /proc/$PID ]" will not evaluate correctly on MACOS, change to "if ! ps -p $PID > /dev/null";
 if ! ps -p $PID > /dev/null; then
 	 echo -e "\n simplej-codegen.out last 10 lines  is :"
     tail -10 ${LOGDIR}/simplej-codegen.out
     echo -e "\033[31m \n$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API start may be unsuccessful, process exited immediately after starting, might be JVM parameter problem or JMX port occupation! See ${LOGDIR}/simplej-codegen.out for more information. \033[0m"
     exit -1
  fi

  CHECK_STATUS=`cat ${STATUS_FILE}`
  starttime=0
  while  [ x"$CHECK_STATUS" == x ]; do
    if [[ "$starttime" -lt ${START_TIME} ]]; then
      sleep 1
      ((starttime++))
      echo -e ".\c"
      CHECK_STATUS=`cat ${STATUS_FILE}`
    else
	  echo -e "\n simplej-codegen.out last 10 lines  is :"
      tail -10 ${LOGDIR}/simplej-codegen.out
	  echo -e "\033[31m \n$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API start maybe unsuccess, start checking not finished until reach the starting timeout! See ${LOGDIR}/simplej-codegen.out for more information. \033[0m"
      exit -1
    fi
  done

  if [ $CHECK_STATUS = "SUCCESS" ]; then
	echo -e "\n$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API start successfully, running as process: $PID."
	echo ${LISTEN_STATUS} > ${STATUS_FILE}
  fi

  if [ $CHECK_STATUS = "ERROR" ]; then
	kill $PID
	echo -e "\n simplej-codegen.out last 10 lines  is :"
    tail -10 ${LOGDIR}/simplej-codegen.out
	echo -e "\033[31m  \n$(date '+%Y-%m-%d %H:%M:%S') $SERVER_API start failed.See ${LOGDIR}/simplej-codegen.out for more information. \033[0m"
  fi

}

STATUS()
{
  if [ -f $PID_FILE ] ; then
	PID=`cat $PID_FILE`
  fi
  if [ "$PID" != "" ]
	then
	if [ -d /proc/$PID ];then
	  LISTEN_STATUS=`cat ${STATUS_FILE}`
	  echo "$SERVER_API is running , PID is ${LISTEN_STATUS}."
	  exit 0
	fi
  fi
  echo "$SERVER_API is not running."
}


case "$CMD" in
  stop) STOP;;
  start) START;;
  restart) STOP;sleep 3;START;;
  status) STATUS;;
  help) USAGE;;
  *) USAGE;;
esac