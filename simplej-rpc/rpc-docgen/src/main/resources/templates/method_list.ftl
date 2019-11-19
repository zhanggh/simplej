<html>
<head>
    <title>rpc在线文档</title>

</head>
<body>
<p>${serviceName}服务方法列表：</p>
<ol>
    <#list methods as method>
        <li><a href="../method/${method.methodId}.html" target="f_method_detail">${method.methodName}</a></li>
    </#list>
</ol>
</body>
</html>