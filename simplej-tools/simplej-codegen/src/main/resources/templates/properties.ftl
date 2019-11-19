app.name = <#if appName??> ${appName}<#else></#if>
server.port=80
#用户回话session过期时间，以秒为单位 server.context-path=/index#配置访问路径，默认为/
server.session-timeout=1000000
#配置Tomcat编码，默认为UTF-8
server.tomcat.uri-encoding=UTF-8
#Tomcat是否开启压缩，默认为关闭
server.tomcat.compression=on
#spring.mvc.view.prefix=/views/
#spring.mvc.view.suffix=.jsp

#关闭默认模板引擎
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true

# http编码指定为utf-8
spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true