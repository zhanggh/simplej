echo off
title simplej-codegen
rem cd ..

rem set APP_HOME=%cd%
rem set APP_LOG_HOME=%APP_HOME%\logs

rem set CLASSPATH="%CLASSPATH%;%APP_GROOVY_DIR%;%APP_CONF_HOME%;"

rem setlocal enabledelayedexpansion
rem for  %%I in ("lib\*.jar") do set CLASSPATH=!CLASSPATH!;%APP_HOME%\%%I

rem set APP_CONF_HOME=%APP_HOME%\conf
rem set APP_TEMP_DIR=%APP_HOME%\tmp


set JMX_PORT=8061
set JAVA_OPTS=-Dsimplej-codegen.logfile=logs\simplej-codegen
set MEM_OPTS=-server -Xms512m -Xmx512m -Xss256K -XX:NewRatio=1 -XX:MaxPermSize=128m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
set GCLOG_OPTS=-Xloggc:..\logs\gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGC
set JMX_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1
echo on
echo "staring server......"
cd ..
java  %JAVA_OPTS%  %MEM_OPTS% %GCLOG_OPTS% %JMX_OPTS% -jar simplej-codegen-1.4.jar

@pause