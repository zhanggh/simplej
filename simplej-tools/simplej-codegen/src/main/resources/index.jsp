<%@page import="java.util.Date" language="java" contentType="text/plain; charset=UTF-8" pageEncoding="utf-8"%><%
String status = "ok0000000000000000";
try {
	System.out.println("健康检查，时间戳：" + new Date(System.currentTimeMillis()));
} catch (Throwable e) {
	System.out.println(status = e.getLocalizedMessage());
}
%><%=status%>