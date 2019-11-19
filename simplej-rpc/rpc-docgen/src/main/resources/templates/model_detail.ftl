<html>
<head>
    <title>rpc在线文档model详情</title>
    <head>
        <style type="text/css">
            table, table tr th, table tr td {
                border: 1px solid #0fefef
            }

            table {
                border-collapse: collapse;
                padding: 2px;
                width: 88%
            }

            thead {
                background-color: deepskyblue
            }
        </style>
    </head>
<body>
<div>
    <h3 style="text-align: center">${model.modelName}接口文档详情</h3>
    <hr>
</div>
<a name="${model.modelId}"><h3>模型:${model.modelName}</h3></a>
<h4>功能：${model.modelDoc!}</h4>
<#if model.createTime != ""><h4>创建时间：${model.createTime!}</h4></#if>
<h4>作者：${model.author!}</h4>
<table>
    <thead>
    <tr>
        <th colspan="6" style="text-align: left">请求参数</th>
    </tr>
    <tr>
        <td>参数名</td>
        <td>数据类型</td>
        <td>是否必填</td>
        <td>最大长度</td>
        <td>默认值</td>
        <td>描述</td>
    </tr>
    </thead>
    <tbody>
    <#list model.paramMetaList as param>
        <tr>
            <td><#if param.structFlag==1><a href="${param.structId}.html" target="f_method_detail">${param.name}</a>
                <#else>${param.name}</#if></td>
            <td>${param.dataType}</td>
            <td><#if param.required>是<#else>否</#if></td>
            <td><#if param.maxLen !=0>${param.maxLen}</#if></td>
            <td>${param.defaultVale!}</td>
            <td>${param.paramDoc!}</td>
        </tr>
    </#list>


    </tbody>
</table>
<br>
<br>
</body>
</html>
