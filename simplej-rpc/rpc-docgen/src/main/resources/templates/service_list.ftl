<html>
<head>
    <title>rpc服务在线文档</title>

</head>
<body>
<p>rpc服务列表：</p>
<ol>
    <#list serviceList as service>
        <li><a href="service_methods/${service.serviceId}_list.html" target="f_method_list">${service.serviceName}</a></li>
    </#list>
</ol>
</body>
</html>